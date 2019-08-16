package com.dy.toktikhelper.utils.spdelegate

import android.content.SharedPreferences
import com.dy.toktikhelper.core.GetResultNotNull
import kotlin.reflect.KProperty

class BooleanSPDelegate(val sharedPreferences: SharedPreferences, val key: String, val defaultBody: GetResultNotNull<Boolean>)  {
    var value: Boolean? = null

    @Synchronized
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
        var value = value
        if(value == null) {
            value = sharedPreferences.getBoolean(key, defaultBody())
            if(!sharedPreferences.contains(key))
                sharedPreferences.edit().putBoolean(key, value).apply()
        }
        return value
    }

    @Synchronized
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
        if(value != this.value) {
            this.value = value
            sharedPreferences.edit().putBoolean(key, value).apply()
        }
    }
}