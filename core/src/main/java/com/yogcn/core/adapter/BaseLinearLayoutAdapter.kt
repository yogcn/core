package com.yogcn.core.adapter

import android.content.Context
import android.view.View
import android.widget.LinearLayout

/**
 * Created by lyndon on 2017/12/13.
 */
abstract class BaseLinearLayoutAdapter<T> : BaseAdapter<T> {

    private var detachView: ArrayList<View>
    private var container: LinearLayout

    constructor(context: Context, container: LinearLayout, data: MutableCollection<T>, layoutRes: Int) : super(context, data, layoutRes) {
        this.container = container
        this.detachView = ArrayList()
        notifyDataSetChanged()
    }

    override fun notifyDataSetChanged() {
        var viewCount = container.childCount
        for (i in 0 until count) {
            if (i < viewCount) {
                getView(i, container.getChildAt(i), container)
            } else {
                var v: View? = null
                if (detachView.size > 0) {
                    v = detachView[0]
                    detachView.removeAt(0)
                }
                v = getView(i, v, container)
                container.addView(v)
            }
        }
        if (viewCount > count) {
            for (i in viewCount downTo count) {
                detachView.add(container.getChildAt(i))
                container.removeViewAt(i)
            }
        }


    }

}