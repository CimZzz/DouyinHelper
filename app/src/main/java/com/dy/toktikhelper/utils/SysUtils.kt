package com.dy.toktikhelper.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import com.dy.toktikhelper.core.TimerBundle
import com.dy.toktikhelper.core.TimerCallback
import java.io.DataOutputStream
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019-08-14 15:37:23
 *  Project : taoke_android
 *  Since Version : Alpha
 */
object SysUtils {
    fun destroy() {
        shutdown()
        closeTimer()
        val tempMap = timerBundleMap
        timerBundleMap = null
        tempMap?.values?.forEach {
            it.isDestroy = true
        }
    }

    private val contactPermission = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)

    fun checkContactPermission(context: Context): Boolean {
        return checkPermission(context, contactPermission)
    }

    fun checkPermission(context: Context, permissions: Array<String>): Boolean {
        var isGrant = true
        for(permission in permissions) {
            isGrant = ContextCompat.checkSelfPermission(context , permission) == PackageManager.PERMISSION_GRANTED
            if(!isGrant)
                break
        }

        return isGrant
    }

    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    fun runOnMain(runnable: () -> Unit) {
        handler.post(runnable)
    }


    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private var timerBundleMap: ConcurrentHashMap<String, TimerBundle>? = ConcurrentHashMap()

    fun registerTimerCallback(key: String, intervalTime: Long, goalTime: Long = -1L, isOnce: Boolean = false, callback: TimerCallback) {
        unregisterTimerCallback(key)
        timerBundleMap?.put(key, TimerBundle(key, intervalTime, goalTime, isOnce, callback))
        ensureStartTimer()
    }

    fun unregisterTimerCallback(key: String) {
        timerBundleMap?.remove(key)?.isDestroy = true
    }

    private fun ensureStartTimer() {
        if(timer != null)
            return
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                val currentTime = SystemClock.elapsedRealtime()
                timerBundleMap?.values?.forEach {
                    if(it.checkTime(currentTime)) {
                        SysUtils.runOnMain {
                            if(!it.isDestroy) {
                                if(it.isOnce || it.checkOver()) {
                                    unregisterTimerCallback(it.key)
                                    it.callback(true)
                                }
                                else it.callback(false)
                            }
                        }
                    }
                }
            }
        }
        timer?.schedule(timerTask, 0L, 200L)
    }

    private fun closeTimer() {
        timerTask?.cancel()
        timerTask = null
        timer?.cancel()
        timer = null
    }


    private var threadPool = Executors.newCachedThreadPool()

    fun run(runnable: () -> Unit) {
        threadPool.execute(runnable)
    }

    private fun shutdown() {
        threadPool?.shutdownNow()
        threadPool = null
    }

    private var process: Process? = null

    private fun closeSu() {
        try {
            process?.destroy()
        }
        catch (e: Throwable) {

        }
    }

    fun execShellCmd(cmd: String) {
        try {
            val process = if(this.process == null) {
                val newProcess = Runtime.getRuntime().exec("su")
                this.process = newProcess
                newProcess
            }
            else this.process!!

            val outputStream = process.outputStream
            val dataOutputStream = DataOutputStream(outputStream)
            dataOutputStream.writeBytes(cmd)
            dataOutputStream.flush()
            dataOutputStream.close()
        }
        catch (e: Throwable) {
            log(e)
        }
    }
}