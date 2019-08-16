package com.dy.toktikhelper.core

import com.dy.toktikhelper.actions.SocketInitErrorAction
import com.dy.toktikhelper.actions.SocketOutAction
import com.dy.toktikhelper.messages.InitMessage
import com.dy.toktikhelper.messages.UnsupportedApiMessage
import com.dy.toktikhelper.utils.DataUtils
import com.dy.toktikhelper.utils.MessageLooper
import com.dy.toktikhelper.utils.MessageResolver
import com.dy.toktikhelper.utils.SysUtils
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.net.Socket

class SocketConnection(private val messageLooper: MessageLooper<BaseAction>) {
    private var socket: Socket? = null
    private var outputStream: OutputStream? = null
    private var clientId: Int = -1
    private var innerMessageLooper: MessageLooper<BaseAction>? = null


    fun reBootstrap() {
        destroy()
        bootstrap()
    }

    fun bootstrap() {
        if(socket != null)
            return
        val host = DataUtils.getHost()
        val port = DataUtils.getPort()

        try {
            val curSocket = Socket(host, port)
            this.socket = curSocket
            val inputStream = curSocket.getInputStream()
            outputStream = curSocket.getOutputStream()
            SysUtils.run {
                /// 循环读取
                try {
                    val byteBuffer = ByteArrayOutputStream()
                    var terminatorCount = 5
                    while (true) {
                        val currentByte = inputStream.read()
                        if(currentByte.toChar() == '&') {
                            terminatorCount--
                            if(terminatorCount == 0) {
                                val message = MessageResolver.parseMessage(byteBuffer.toByteArray())
                                terminatorCount = 5
                                byteBuffer.reset()

                                if(message == null)
//                                    innerMessageLooper.sendAction(SocketOutAction(UnsupportedApiMessage()))
                                else onRecvMessage(message)
                            }
                        }
                        else {
                            if(terminatorCount != 5)
                                (terminatorCount..5).forEach { _ -> byteBuffer.write('&'.toInt()) }
                            byteBuffer.write(currentByte)
                        }
                    }
                }
                catch (e: Throwable) {
                    messageLooper.sendAction(SocketInitErrorAction)
                }
            }
        }
        catch (e: Exception) {
            socket = null
            outputStream = null
            messageLooper.sendAction(SocketInitErrorAction)
        }
    }


    fun destroy() {
        try {
            socket?.close()
            socket = null
            outputStream = null
        }
        catch (e: Throwable) {

        }
    }

    private fun onRecvMessage(message: BaseMessage) {
        when(message) {
            is InitMessage -> {
                clientId = message.clientId
            }
        }
    }
}