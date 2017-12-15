package com.yogcn.core.util

import android.util.Log
import com.yogcn.core.base.BaseApplication

/**
 * Created by lyndon on 2017/12/12.
 */
object Log {

    private val logEnable = BaseApplication.instance.isDebug()

    fun v(tag: String, msg: String?) {
        if (logEnable)
            Log.v(tag, msg)
    }
    fun d(tag: String, msg: String?) {
        if (logEnable)
            Log.d(tag, msg)
    }
    fun i(tag: String, msg: String?) {
        if (logEnable)
            Log.i(tag, msg)
    }

    fun w(tag: String, msg: String?) {
        if (logEnable)
            Log.w(tag, msg)
    }

    fun e(tag: String, msg: String?) {
        if (logEnable)
            Log.e(tag, msg)
    }
}