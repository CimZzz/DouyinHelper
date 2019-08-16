package com.dy.toktikhelper.core

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.dy.toktikhelper.accessibility.ActionAccessibility
import com.dy.toktikhelper.utils.DataUtils
import com.dy.toktikhelper.utils.SysUtils

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/1/18 12:16:04
 *  Project : taoke_android
 *  Since Version : Alpha
 */
class SubscribeService : AccessibilityService() {

    private lateinit var currentAccessibility: BaseAccessibility

    override fun onCreate() {
        super.onCreate()
        DataUtils.init(this)
        currentAccessibility = ActionAccessibility(this)
    }

    override fun onInterrupt() {
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        currentAccessibility.onEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        SysUtils.destroy()
    }

    fun fireOtherAccessibility(accessibility: BaseAccessibility) {
        currentAccessibility.onHidden()
        currentAccessibility = accessibility
        accessibility.onFired()
    }
}