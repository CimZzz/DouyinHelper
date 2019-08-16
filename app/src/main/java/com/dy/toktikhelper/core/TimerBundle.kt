package com.dy.toktikhelper.core

import android.os.SystemClock

class TimerBundle(val key: String, val intervalTime: Long, val goalTime: Long, val isOnce: Boolean, val callback: TimerCallback) {
    val startTime = SystemClock.elapsedRealtime()
    var lastTime: Long = startTime
    var isDestroy: Boolean = false

    fun checkTime(currentTime: Long): Boolean {
        if(isDestroy)
            return false
        if(currentTime - lastTime >= intervalTime) {
            lastTime = currentTime
            return true
        }

        return false
    }

    fun checkOver(): Boolean =
            if(goalTime == -1L)
                false
            else lastTime - startTime >= goalTime
}