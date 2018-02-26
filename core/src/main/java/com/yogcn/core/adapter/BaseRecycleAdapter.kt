package com.yogcn.core.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.SparseArray
import android.view.ViewGroup
import com.yogcn.core.base.RecyclerHolder
import com.yogcn.core.base.ViewHolder

/**
 * Created by lyndon on 2017/12/13.
 */
abstract class BaseRecycleAdapter<T> : RecyclerView.Adapter<RecyclerHolder> {

    var headerHolder: SparseArray<ViewHolder>
    var footerHolder: SparseArray<ViewHolder>

    protected var data: MutableCollection<T>
    protected val context: Context
    private var layoutRes: Int

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_FOOTER = 90000000
    }

    constructor(recyclerView: RecyclerView, data: MutableCollection<T>, layoutRes: Int) {
        this.context = recyclerView.context
        this.data = data
        this.layoutRes = layoutRes
        this.headerHolder = SparseArray()
        this.footerHolder = SparseArray()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
        //header
            isHeader(position) -> headerHolder.keyAt(position)
        //footer
            isFooter(position) -> footerHolder.keyAt(position - data.size - headerHolder.size())
        //item
            else -> layoutRes
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerHolder {
        return when {
        //header
            null != headerHolder[viewType] -> RecyclerHolder(headerHolder[viewType])
        //footer
            null != footerHolder[viewType] -> RecyclerHolder(footerHolder[viewType])
        //item
            else -> RecyclerHolder(ViewHolder(context, parent, layoutRes, false))
        }
    }


    override fun onBindViewHolder(holder: RecyclerHolder?, position: Int) {
        when {
        //header
            isHeader(position) -> {

            }
        //footer
            isFooter(position) -> {

            }
        //item
            else -> {
                bindData(holder?.holder, getItem(position - headerHolder.size()), position)
                holder?.holder?.dataBinding?.executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int {
        return headerHolder.size() + data.size + footerHolder.size()
    }

    private fun getItem(position: Int): T {
        return ArrayList<T>(data)[position]
    }

    private fun isHeader(position: Int): Boolean {
        return headerHolder.size() > 0 && position < headerHolder.size()
    }

    private fun isFooter(position: Int): Boolean {
        return footerHolder.size() > 0 && position >= (headerHolder.size() + data.size)
    }

    /**
     * 添加header
     * @param holder
     */
    fun addHeader(holder: ViewHolder?) {
        if (null != holder) {
            headerHolder.put(TYPE_HEADER + headerHolder.size(), holder)
        }
    }

    /**
     * 添加footer
     * @param holder
     */
    fun addFooter(holder: ViewHolder?) {
        if (null != holder) {
            footerHolder.put(TYPE_FOOTER + footerHolder.size(), holder)
        }
    }


    /**
     * 绑定业务数据到布局控件上
     * @param holder
     * @param item
     * @param position
     */
    abstract fun bindData(holder: ViewHolder?, item: T, position: Int)


    /**
     * 添加数据,刷新数据列表
     * @param data
     */
    fun addData(data: MutableCollection<T>?) {
        data?.forEach {
            var position = this.headerHolder.size() + this.data.size
            this.data.add(it)
            notifyItemInserted(position)
        }
    }

    fun clear() {
        this.data.clear()
        notifyDataSetChanged()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        var layoutManager = recyclerView?.layoutManager
        if (layoutManager is GridLayoutManager) {
            var lookup = layoutManager.spanSizeLookup
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    var itemViewType = getItemViewType(position)
                    return if (headerHolder[itemViewType] != null || footerHolder[itemViewType] != null) {
                        layoutManager.spanCount
                    } else {
                        lookup?.getSpanSize(position) ?: 1
                    }
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerHolder?) {
        super.onViewAttachedToWindow(holder)
        val position = holder?.layoutPosition
        var itemViewType = getItemViewType(position!!)
        if (headerHolder[itemViewType] != null || footerHolder[itemViewType] != null) {
            val lp = holder?.itemView?.layoutParams
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                lp.isFullSpan = true
            }
        }
    }
}