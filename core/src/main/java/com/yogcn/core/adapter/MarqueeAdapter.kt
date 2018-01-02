package com.yogcn.core.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.yogcn.core.base.ViewHolder
import java.util.ArrayList

/**
 * Created by lyndon on 2017/12/20.
 */
abstract class MarqueeAdapter<T> : PagerAdapter {
    protected var context: Context
    private var data: MutableCollection<T>
    private var layoutRes: Int = 0

    constructor(context: Context, data: MutableCollection<T>, layoutRes: Int) {
        this.context = context
        this.data = data
        this.layoutRes = layoutRes
    }

    override fun getCount() = Int.MAX_VALUE

    open fun getDataCount() = data.size

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    open fun getItem(position: Int): T? {
        return if (data.isEmpty())
            null
        else
            ArrayList(data)[position % getDataCount()]
    }

    override fun getItemPosition(`object`: Any?): Int {
        return if (data.isEmpty())
            POSITION_NONE
        else
            POSITION_UNCHANGED
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any? {
        var holder = ViewHolder(context, container, layoutRes, true)
        if (data.isNotEmpty()) {
            var position = position % getDataCount()
            bindData(holder, getItem(position), position)
        }
        return holder.dataBinding.root
    }

    abstract fun bindData(holder: ViewHolder?, t: T?, position: Int)

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        var view = `object` as View
        container?.removeView(view)
    }
}