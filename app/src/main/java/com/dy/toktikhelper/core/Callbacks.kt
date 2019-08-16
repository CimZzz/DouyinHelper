package com.dy.toktikhelper.core


typealias TravelCallback<T> = (T) -> Boolean

typealias TimerCallback = (Boolean) -> Unit

typealias GetResultNotNull<T> = () -> T