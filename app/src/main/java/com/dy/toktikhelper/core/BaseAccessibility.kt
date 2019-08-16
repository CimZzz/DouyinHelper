package com.dy.toktikhelper.core

import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.dy.toktikhelper.utils.SysUtils
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 *  Anchor : Create by CimZzz
 *  Time : 2019/1/18 18:21:51
 *  Project : taoke_android
 *  Since Version : Alpha
 */
abstract class BaseAccessibility(private val service: SubscribeService) {
    open fun onFired() {

    }

    open fun onEvent(event: AccessibilityEvent) {

    }

    open fun onHidden() {

    }

    fun getContext(): Context = service

    fun getRootWindow(): AccessibilityNodeInfo = service.rootInActiveWindow

    fun fireAccessibility(accessibility: BaseAccessibility) {
        service.fireOtherAccessibility(accessibility)
    }

    fun registerTimer(key: String, intervalTime: Long, goalTime: Long = -1L, isOnce: Boolean = false, callback: TimerCallback) {
        SysUtils.registerTimerCallback(key, intervalTime, goalTime, isOnce, callback = callback)
    }

    fun unregisterTimer(key: String) {
        SysUtils.unregisterTimerCallback(key)
    }

    fun travelSelfChild(node: AccessibilityNodeInfo = getRootWindow(), travelCallback: TravelCallback<AccessibilityNodeInfo>) {
        if(travelCallback(node))
            travelChild(node, travelCallback)
    }

    fun travelChild(node: AccessibilityNodeInfo = getRootWindow(), travelCallback: TravelCallback<AccessibilityNodeInfo>) {
        if(node.childCount != 0) {
            val list = LinkedList<AccessibilityNodeInfo>()
            list.addAll((0 until node.childCount).map { node.getChild(it) })
            while (list.isNotEmpty()) {
                val childNode = list.removeFirst()?:continue
                if(!travelCallback(childNode))
                    break

                if(childNode.childCount != 0)
                    list.addAll((0 until childNode.childCount).map { childNode.getChild(it) })
            }
        }
    }

    fun findViewByDescription(node: AccessibilityNodeInfo = getRootWindow(), description: String): AccessibilityNodeInfo? {
        var founded: AccessibilityNodeInfo? = null
        travelSelfChild(node) {
                childNode ->
            if(childNode.contentDescription != null && childNode.contentDescription.startsWith(description)) {
                founded = childNode
                false
            }
            else true
        }

        return founded
    }

    fun findViewByText(node: AccessibilityNodeInfo = getRootWindow(), text: String): AccessibilityNodeInfo? {
        var founded: AccessibilityNodeInfo? = null
        travelSelfChild(node) {
                childNode ->
            if(childNode.text != null && childNode.text.startsWith(text)) {
                founded = childNode
                false
            }
            else true
        }

        return founded
    }

    fun findViewByFirstClassName(node: AccessibilityNodeInfo = getRootWindow(), clsName: String): AccessibilityNodeInfo? {
        var founded: AccessibilityNodeInfo? = null
        travelSelfChild(node) {
                childNode ->
            if(childNode.className == clsName) {
                founded = childNode
                false
            }
            else true
        }

        return founded
    }

    fun findViewByClassName(node: AccessibilityNodeInfo = getRootWindow(), clsName: String): AccessibilityNodeInfo? {
        (0 until node.childCount).forEach {
            val childNode = node.getChild(it)
            if(childNode.className == clsName)
                return childNode
        }

        return null
    }

    fun findViewByIdx(node: AccessibilityNodeInfo = getRootWindow(), idx: Int): AccessibilityNodeInfo? {
        if(idx >= node.childCount)
            return null

        return node.getChild(idx)
    }

    fun findViewParentIdx(node: AccessibilityNodeInfo): Int {
        if(node.parent == null)
            return -1

        for(i in 0 until node.parent.childCount) {
            val childNode = node.parent.getChild(i)
            if(childNode == node)
                return i
        }

        return -1
    }

    fun findEmbedView(node: AccessibilityNodeInfo = getRootWindow(), vararg params: Any): AccessibilityNodeInfo? {
        var currentNode: AccessibilityNodeInfo = node
        for(param in params) {
            if(param is Int) {
                currentNode = findViewByIdx(currentNode, param)?:return null
            }
            else if(param is String) {
                currentNode = findViewByClassName(currentNode, param)?:return null
            }
        }

        return currentNode
    }

    fun getNodeIdxArr(node: AccessibilityNodeInfo, terminator: AccessibilityNodeInfo = getRootWindow()): Array<Int>? {
        var tempNode = node
        val list = LinkedList<Int>()
        while (tempNode.parent != null && tempNode != terminator) {
            val idx = findViewParentIdx(tempNode)
            if(idx == -1)
                return null

            list.add(idx)
            tempNode = tempNode.parent
        }

        return list.toTypedArray().reversedArray()
    }

    fun convertJSON(node: AccessibilityNodeInfo = getRootWindow()): JSONObject {
        return recurseJSONObject(node)
    }

    private fun recurseJSONObject(node: AccessibilityNodeInfo, idxOfParent: Int = -1): JSONObject {
        val obj = JSONObject()
        if(idxOfParent != -1)
            obj.put("parentIdx", idxOfParent)
        obj.put("text", node.text)
        obj.put("description", node.contentDescription)
        obj.put("class", node.className)
        obj.put("id", node.viewIdResourceName)

        if(node.childCount != 0) {
            val childArray = JSONArray()
            (0 until node.childCount).forEach { idx ->
                childArray.put(recurseJSONObject(node.getChild(idx), idxOfParent))
            }
            obj.put("child", childArray)
        }

        return obj
    }
}
