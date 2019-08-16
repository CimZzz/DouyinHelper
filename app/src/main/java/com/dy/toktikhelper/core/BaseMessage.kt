package com.dy.toktikhelper.core

import android.support.annotation.CallSuper
import java.io.DataInputStream
import java.io.DataOutputStream

open class BaseMessage {
    var clientId: Int = -1
    var messageType: Int = 0
    var reqId: Int = 0

    @CallSuper
    fun onDecode(output: DataOutputStream) {
        output.write(clientId)
        output.write(messageType)
        output.write(reqId)
    }

    @CallSuper
    fun onEncode(input: DataInputStream) {
        clientId = input.readInt()
        messageType = input.readInt()
        reqId = input.readInt()
    }
}