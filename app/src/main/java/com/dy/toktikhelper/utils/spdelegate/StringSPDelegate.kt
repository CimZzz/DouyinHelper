package com.dy.toktikhelper.utils.spdelegate

import android.content.SharedPreferences
import com.dy.toktikhelper.core.GetResultNotNull
import kotlin.reflect.KProperty

class StringSPDelegate(val sharedPreferences: SharedPreferences, val key: String, val defaultBody: GetResultNotNull<String>? = null) {
    var value: String? = null

    @Synchronized
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        if(value == null) {
            value = sharedPreferences.getString(key, null)
            if (value == null) {
                value = defaultBody?.let { it() }
                if (value != null)
                    sharedPreferences.edit().putString(key, value).apply()
            }
        }
        return value
    }

    @Synchronized
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        if(value != this.value) {
            this.value = value
            sharedPreferences.edit().putString(key, value).apply()
        }
    }
}