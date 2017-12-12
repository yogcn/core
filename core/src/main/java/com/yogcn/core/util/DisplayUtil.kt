package com.yogcn.core.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by lyndon on 2017/12/12.
 */
class DisplayUtil {

    private var designWidth = 1080f
    private var designHeight = 1920f
    var scale = 1.0f
    var width = 1080
    var height = 1920
    var density = 1.0f

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = DisplayUtil()
    }

    fun setDesignWidth(designWidth: Float): DisplayUtil {
        this.designWidth = designWidth
        return this
    }

    fun setDesignHeight(designHeight: Float): DisplayUtil {
        this.designHeight = designHeight
        return this
    }

    fun init(context: Context) {
        var metrics = DisplayMetrics()
        var windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)

        width = metrics.widthPixels
        height = metrics.heightPixels
        density = metrics.density

        scale = Math.min(width / designWidth, height / designHeight)
    }

    fun scale(px: Int) = Math.round(scale * px)

    fun scale(px: Float) = scale * px


    fun teminalScreenInfo(): String {
        return "the terminal screen info is width=$width\nheight=$height\ndensity=$density"
    }

}