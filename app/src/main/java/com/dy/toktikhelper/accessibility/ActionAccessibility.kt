package com.dy.toktikhelper.accessibility

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.inputmethod.InputMethodManager
import com.dy.toktikhelper.core.BaseAccessibility
import com.dy.toktikhelper.core.Constants
import com.dy.toktikhelper.core.SubscribeService
import com.dy.toktikhelper.utils.SysUtils
import com.dy.toktikhelper.utils.log

class ActionAccessibility(service: SubscribeService) : BaseAccessibility(service) {
    override fun onEvent(event: AccessibilityEvent) {
        super.onEvent(event)
        when(event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                when(event.className) {
                    Constants.CNAME.SearchResultActivity -> {
                        checkPreparing()
                    }
                }
            }
        }




        if(event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
            log("${event.className}")

        log("eventType: ${event.eventType}")
    }





    private fun checkPreparing() {
        val editTxt = findViewByFirstClassName(clsName = Constants.CNAME.EDIT_TEXT)?:return
        val searchBtn = findViewByText(text = "搜索")?:return
        val userTab = findViewByText(text = "用户")?:return
        val bundle = Bundle()
        bundle.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "hello world")
        editTxt.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle)
        userTab.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        val rect = Rect()
        searchBtn.getBoundsInScreen(rect)
        SysUtils.execShellCmd("input tap ${rect.width() / 2 + rect.left} ${rect.height() / 2 + rect.top}")
    }
}