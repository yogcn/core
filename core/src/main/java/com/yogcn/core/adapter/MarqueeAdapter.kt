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
    protected var data: MutableCollection<T>
    private var layoutRes: Int = 0

    constructor(context: Context, data: MutableCollection<T>, layoutRes: Int) {
        this.context = context
        var list = ArrayList(data)
        list.add(0, list[data.size - 1])
        list.add(list[1])
        this.data = list
        this.layoutRes = layoutRes
    }

    override fun getCount() = data.size

    override fun isViewFromObject(view: View?, `object`: Any?) = view == `object`

    fun getItem(position: Int): T {
        return ArrayList(data)[position]
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        var position = position % count
        var holder = ViewHolder(context, container, layoutRes, false)
        bindData(holder, getItem(position), position)
        return holder.dataBinding.root
    }

    abstract fun bindData(holder: ViewHolder?, t: T, position: Int)

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        var view = `object` as View
        container?.removeView(view)
    }
}