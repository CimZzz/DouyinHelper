package com.dy.toktikhelper.utils.spdelegate

import android.content.SharedPreferences
import com.dy.toktikhelper.core.GetResultNotNull
import kotlin.reflect.KProperty

class LongSPDelegate(val sharedPreferences: SharedPreferences, val key: String, val defaultBody: GetResultNotNull<Long>)  {
    var value: Long? = null

    @Synchronized
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Long {
        var value = value
        if(value == null) {
            value = sharedPreferences.getLong(key, defaultBody())
            if(!sharedPreferences.contains(key))
                sharedPreferences.edit().putLong(key, value).apply()
        }
        return value
    }

    @Synchronized
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
        if(value != this.value) {
            this.value = value
            sharedPreferences.edit().putLong(key, value).apply()
        }
    }
}