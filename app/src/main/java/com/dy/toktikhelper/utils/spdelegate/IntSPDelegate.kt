package com.dy.toktikhelper.utils.spdelegate

import android.content.SharedPreferences
import com.dy.toktikhelper.core.GetResultNotNull
import kotlin.reflect.KProperty

class IntSPDelegate(val sharedPreferences: SharedPreferences, val key: String, val defaultBody: GetResultNotNull<Int>)  {
    var value: Int? = null

    @Synchronized
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        var value = value
        if(value == null) {
            value = sharedPreferences.getInt(key, defaultBody())
            if(!sharedPreferences.contains(key))
                sharedPreferences.edit().putInt(key, value).apply()
        }
        return value
    }

    @Synchronized
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if(value != this.value) {
            this.value = value
            sharedPreferences.edit().putInt(key, value).apply()
        }
    }
}