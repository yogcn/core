package com.yogcn.core.util

import android.app.Activity
import java.util.*

/**
 * Created by lyndon on 2017/10/30.
 */

class ActivityStack {
    private var activityStack: Stack<Activity> = Stack()

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = ActivityStack()
    }


    /**
     * 添加到堆栈
     * @param activity
     */
    fun put(activity: Activity) {
        if (activity !in activityStack)
            activityStack.push(activity)
    }

    /**
     * 移除堆栈
     * @param activity
     */
    fun remove(activity: Activity) {
        if (activity in activityStack)
            activityStack.remove(activity)
    }

    /**
     * 清空堆栈
     */
    fun clear() {
        for (activity in activityStack)
            activity.finish()
        activityStack.clear()
    }

    /**
     * 获取activity
     */
    fun getActivity(index: Int): Activity? {
        return if (index < size())
            activityStack[index]
        else
            null
    }


    /**
     * 获取当前activity
     */
    fun getCurrentActivity(): Activity? {
        return if (activityStack.isNotEmpty())
            activityStack.lastElement()
        else
            null
    }

    /**
     * 获取堆栈大小
     */
    fun size() = activityStack.size

}
