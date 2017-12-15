package com.yogcn.demo.adapter

import android.support.v7.widget.RecyclerView
import com.yogcn.core.adapter.BaseRecycleAdapter
import com.yogcn.core.base.ViewHolder
import com.yogcn.demo.BR

/**
 * Created by lyndon on 2017/12/15.
 */
class StringAdapter : BaseRecycleAdapter<String> {

    constructor(recyclerView: RecyclerView, data: MutableCollection<String>, layoutRes: Int) : super(recyclerView, data, layoutRes)

    override fun bindData(holder: ViewHolder?, item: String, position: Int) {
        holder?.dataBinding?.setVariable(BR.item, item)
    }
}