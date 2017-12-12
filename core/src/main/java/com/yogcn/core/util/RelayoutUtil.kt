package com.yogcn.core.util

import android.graphics.drawable.NinePatchDrawable
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by lyndon on 2017/12/12.
 */
object RelayoutUtil {
    /**
     * 缩放控件
     * @param view
     */
    fun reLayoutViewHierarchy(view: View?) {
        if (null == view)
            return
        scale(view)
        if (view is ViewGroup) {
            var field = ViewGroup::class.java.getDeclaredField("mChildren")
            field.isAccessible = true
            var childRen = field.get(view) as Array<View>
            childRen.forEach {
                reLayoutViewHierarchy(it)
            }
        }
    }

    /**
     * 缩放控件
     * @param view
     */
    fun scale(view: View?) {
        if (null == view)
            return
        if (view is TextView)
            scaleTextView(view)
        if (view.background !is NinePatchDrawable) {
            var leftPadding = DisplayUtil.getInstance().scale(view.paddingLeft)
            var rightPadding = DisplayUtil.getInstance().scale(view.paddingRight)
            var topPadding = DisplayUtil.getInstance().scale(view.paddingTop)
            var bottomPadding = DisplayUtil.getInstance().scale(view.paddingBottom)
            view.setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
        }
        reLayoutLayoutParams(view.layoutParams)
        if (view.minimumHeight > 0) {
            view.minimumHeight = DisplayUtil.getInstance().scale(view.minimumHeight)
        }
        if (view.minimumWidth > 0) {
            view.minimumWidth = DisplayUtil.getInstance().scale(view.minimumWidth)
        }
    }

    /**
     * 设置控件间距
     * @param layoutParams
     */
    fun reLayoutLayoutParams(layoutParams: ViewGroup.LayoutParams?) {
        if (null == layoutParams)
            return
        if (layoutParams.width > 0) {
            layoutParams.width = DisplayUtil.getInstance().scale(layoutParams.width)
        }
        if (layoutParams.height > 0) {
            layoutParams.height = DisplayUtil.getInstance().scale(layoutParams.height)
        }
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            if (layoutParams.leftMargin > 0) {
                layoutParams.leftMargin = DisplayUtil.getInstance().scale(layoutParams.leftMargin)
            }
            if (layoutParams.rightMargin > 0) {
                layoutParams.rightMargin = DisplayUtil.getInstance().scale(layoutParams.rightMargin)
            }
            if (layoutParams.topMargin > 0) {
                layoutParams.topMargin = DisplayUtil.getInstance().scale(layoutParams.topMargin)
            }
            if (layoutParams.bottomMargin > 0) {
                layoutParams.bottomMargin = DisplayUtil.getInstance().scale(layoutParams.bottomMargin)
            }
        }
    }

    /**
     * 设置控件字体大小
     * @param view
     */
    fun scaleTextView(view: TextView) {
        if (null == view)
            return
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, DisplayUtil.getInstance().scale(view.textSize))
    }

    /**
     * 设置控件上下左右图标大小
     * @param view
     * @param width
     * @param height
     *
     */
    fun setCompoundDrawable(view: View, width: Int, height: Int) {
        when (view) {
            is TextView -> {
                var drawables = view.compoundDrawables
                drawables.filter {
                    it != null
                }.forEach {
                    it.setBounds(0, 0, width, height)
                }
                view.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3])
            }
            is ViewGroup -> {
                var count = view.childCount
                while (count > 0) {
                    val child = view.getChildAt(count - 1)
                    setCompoundDrawable(child, width, height)
                    count--
                }
            }
        }
    }

}