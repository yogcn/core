package com.yogcn.demo.adapter

import android.support.v7.widget.RecyclerView
import com.yogcn.core.adapter.BaseRecycleAdapter
import com.yogcn.core.base.ViewHolder

/**
 * Created by lyndon on 2018/1/19.
 */
class ImageAdapter : BaseRecycleAdapter<String?> {
    var width = 0
    var height = 0

    constructor(recyclerView: RecyclerView, data: MutableCollection<String?>, layoutRes: Int) : super(recyclerView, data, layoutRes)

    override fun bindData(holder: ViewHolder?, item: String?, position: Int) {

    }

}