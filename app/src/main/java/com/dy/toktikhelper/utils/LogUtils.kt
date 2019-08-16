package com.dy.toktikhelper.utils

import android.util.Log

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019-08-14 14:58:06
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object LogUtils {
    fun log(any: Any?) {
        Log.v("CimZzz", any.toString())
    }
}

fun log(any: Any?) {
    LogUtils.log(any)
}