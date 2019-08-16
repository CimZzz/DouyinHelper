package com.dy.toktikhelper.utils

import java.util.concurrent.LinkedBlockingQueue

typealias MessageHandler<T> = (T) -> Unit
typealias MessageHandlerWidthObject<T, E> = (T, E) -> Unit

class MessageLooper<T> (
    private val handler: MessageHandler<T>
) {
    private var state: LooperState = LooperState.Init
    private val messageQueue: LinkedBlockingQueue<T> = LinkedBlockingQueue()
    private val locker = java.lang.Object()

    fun startAsync() {
        synchronized(this) {
            if(state != LooperState.Init)
                return
            state = LooperState.StartAsync

            SysUtils.run {
                start()
            }
        }
    }

    fun start() {
        synchronized(this) {
            if(state != LooperState.Init && state != LooperState.StartAsync)
                return

            state = LooperState.Start
        }


        while (state != LooperState.Destroy) {
            while (messageQueue.isNotEmpty()) {
                val message = messageQueue.poll()
                handler(message)
            }

            synchronized(locker) {
                if(state != LooperState.Destroy && messageQueue.isEmpty()) {
                    state = LooperState.Sleep
                    locker.wait()
                }
            }
        }
    }

    fun sendAction(message: T) {
        if(state == LooperState.Destroy)
            return
        messageQueue.put(message)
        synchronized(locker) {
            if(state == LooperState.Sleep) {
                state = LooperState.Start
                locker.notifyAll()
            }
        }
    }

    fun destroy() {
        synchronized(locker) {
            messageQueue.clear()
            state = LooperState.Destroy
            locker.notifyAll()
        }
    }
}

enum class LooperState {
    Init,
    StartAsync,
    Start,
    Sleep,
    Destroy
}