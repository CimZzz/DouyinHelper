package com.dy.toktikhelper.utils

import com.dy.toktikhelper.core.BaseMessage
import java.io.ByteArrayInputStream
import java.io.DataInputStream

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019-08-16 17:28:16
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object MessageResolver {
    fun parseMessage(byteArray: ByteArray): BaseMessage? {
        try {
            val dataInputStream = DataInputStream(ByteArrayInputStream(byteArray))
        }
        catch (e: Throwable) {

        }

        return null
    }
}