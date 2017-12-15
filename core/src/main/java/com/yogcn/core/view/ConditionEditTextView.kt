package com.yogcn.core.view

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.yogcn.core.R

/**
 * Created by lyndon on 2017/11/5.
 */
class ConditionEditTextView : AppCompatEditText {

    interface ConditionListener {
        fun changeSuccess(@LayoutRes viewId: Int, success: Boolean)
    }

    private lateinit var listener: ConditionListener

    private var minLength: Int = 0

    var success = false

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        var a = context.obtainStyledAttributes(attrs, R.styleable.ConditionEditTextView)
        minLength = a.getInt(R.styleable.ConditionEditTextView_minInput, 0)
        a?.recycle()
        init()
    }

    constructor(context: Context?) : super(context) {
        init()
    }


    private fun init() {
        success = this.text.toString().trim().length >= minLength
        addTextChangedListener(object : TextWatcher {


            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                success = (s.length >= minLength)
                if (null != listener) {
                    listener.changeSuccess(id, success)
                }
            }
        })
    }

    fun addChangeListener(conditionListener: ConditionListener) {
        this.listener = conditionListener
    }

}