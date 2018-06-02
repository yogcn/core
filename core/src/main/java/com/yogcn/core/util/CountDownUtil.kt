package com.yogcn.core.util

import android.os.Handler
import android.transition.Scene
import android.widget.TextView

/**
 * Created by pubing on 2018/1/6.
 */
object CountDownUtil : Runnable {
    private var second = 60
    private var nowSecond = second
    private var cutDownListener: CutDownListener? = null
    private var downView: TextView? = null
    private var hint: String = "%sS"
    private var defaultHint: String = "获取验证码"
    private var handler: Handler? = null
    var downing = false

    interface CutDownListener {
        fun onStart(downView: TextView?)
        fun onStop(downView: TextView?)
    }

    fun setDownView(view: TextView?): CountDownUtil {
        this.downView = view
        return this
    }

    fun setHint(hint: String): CountDownUtil {
        this.hint = hint
        return this
    }

    fun setDefaultHint(defaultHint: String): CountDownUtil {
        this.defaultHint = defaultHint
        return this
    }

    fun setSecond(second: Int): CountDownUtil {
        this.second = nowSecond
        this.nowSecond = second
        return this
    }

    fun setListener(cutDownListener: CutDownListener): CountDownUtil {
        this.cutDownListener = cutDownListener
        return this
    }

    fun onStart() {
        handler = Handler()
        downView?.text = String.format(hint, nowSecond)
        downing = true
        cutDownListener?.onStart(downView)
        handler?.postDelayed(this, 1000)
    }

    fun onStop() {
        downView?.text = defaultHint
        nowSecond = second
        downing = false
        cutDownListener?.onStop(downView)
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

    override fun run() {
        if (nowSecond == 0) {
            onStop()
        } else {
            nowSecond--
            downView?.text = String.format(hint, nowSecond)
            handler?.postDelayed(this, 1000)
        }
    }
}
