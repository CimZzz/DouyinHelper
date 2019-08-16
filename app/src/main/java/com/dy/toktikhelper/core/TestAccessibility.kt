package com.dy.toktikhelper.core

import android.view.accessibility.AccessibilityEvent
import com.dy.toktikhelper.utils.log


class TestAccessibility(service: SubscribeService) : BaseAccessibility(service) {
    override fun onEvent(event: AccessibilityEvent) {

        when(event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
//                Toast.makeText(getContext(), event.eventType, Toast.LENGTH_SHORT).show()
                log("Window state changed: 222${event.className}")
            }
        }
    }
}