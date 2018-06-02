package com.yogcn.core.view

import android.content.Context
import android.support.annotation.IdRes
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Created by lyndon on 2018/1/5.
 */
class ConditionLayout : RelativeLayout, ConditionEditTextView.ConditionListener {
    var success = false
    var listener: FormListener? = null

    interface FormListener {
        fun viewSuccess(layout: ConditionLayout, @IdRes viewId: Int, success: Boolean)
        fun formSuccess(success: Boolean)
    }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun changeSuccess( @IdRes viewId: Int, success: Boolean) {
        if (!success) {
            this.success = false
            listener?.viewSuccess(this, viewId, success)
        } else {
            var count = childCount
            var conditionSuccess = true
            (0..count).map { getChildAt(it) }.forEach {
                if (it is ConditionEditTextView) {
                    listener?.viewSuccess(this, it.id, it.success)
                    if (!it.success)
                        conditionSuccess = false
                }
            }
            this.success = conditionSuccess
        }
        listener?.formSuccess(this.success)
    }

    fun init(): ConditionLayout {
        var count = childCount
        (0..count)
                .map { getChildAt(it) }
                .forEach {
                    if (it is ConditionEditTextView) {
                        it.addChangeListener(this)
                    }
                }
        return this
    }
}

