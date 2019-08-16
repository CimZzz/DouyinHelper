package com.dy.toktikhelper.utils

import android.content.Context
import android.content.SharedPreferences
import com.dy.toktikhelper.utils.spdelegate.StringSPDelegate


object DataUtils {
    private lateinit var sp: SharedPreferences


    fun init(context: Context) {
        sp = context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    }


    fun getHost(): String = sp.getString("HOST", null)?:"10.91.3.150"

    fun setHost(host: String) {
        sp.edit().putString("HOST", host).apply()
    }


    fun getPort(): Int = sp.getInt("PORT", 8888)

    fun setPort(port: Int) {
        sp.edit().putInt("PORT", port).apply()
    }
}