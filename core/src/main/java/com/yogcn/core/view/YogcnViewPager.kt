package com.yogcn.core.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by lyndon on 2017/11/9.
 */
class YogcnViewPager : ViewPager {
    var noScroll = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return when (noScroll) {
            true -> false
            false -> super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return when (noScroll) {
            true -> false
            false -> super.onInterceptTouchEvent(ev)
        }
    }
}