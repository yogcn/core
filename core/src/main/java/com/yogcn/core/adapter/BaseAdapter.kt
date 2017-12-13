package com.yogcn.core.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.yogcn.core.base.ViewHolder

/**
 * Created by lyndon on 2017/12/13.
 */
abstract class BaseAdapter<T> : BaseAdapter {
    protected var data: MutableCollection<T>
    protected var context: Context
    private var layoutRes: Int

    /**
     * @param context
     * @param data
     * @param layoutRes
     */
    constructor(context: Context, data: MutableCollection<T>, layoutRes: Int) {
        this.context = context
        this.layoutRes = layoutRes
        this.data = data
    }

    /**
     * 获取adapter当前item总数
     */
    override fun getCount() = data.size

    /**
     * 获取指定位置数据
     * @param position 指定位置
     */
    override fun getItem(position: Int) = ArrayList<T>(data)[position]


    /**
     * 获取指定位置布局控件
     * @param position
     * @param convertView
     * @param parent
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder = ViewHolder.get(context, convertView, parent, layoutRes, position)
        bindData(holder, getItem(position), position)
        return holder.dataBinding.root
    }

    /**
     * 绑定业务数据到布局控件上
     * @param holder
     * @param item
     * @param position
     */
    abstract fun bindData(holder: ViewHolder, item: T, position: Int)


    /**
     * 添加数据,刷新数据列表
     * @param data
     */
    fun addData(data: MutableCollection<T>?) {
        if (null != data) {
            this.data.addAll(data)
            notifyDataSetChanged()
        }
    }


}