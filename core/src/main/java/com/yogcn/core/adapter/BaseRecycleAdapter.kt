package com.yogcn.core.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.yogcn.core.base.RecyclerHolder

/**
 * Created by lyndon on 2017/12/13.
 */
abstract class BaseRecycleAdapter<T>{}
//    : RecyclerView.Adapter<RecyclerHolder> {
//    protected var data: MutableCollection<T>
//    protected var context: Context
//    private var layoutRes: Int
//
//    constructor(recyclerView: RecyclerView, data: MutableCollection<T>, layoutRes: Int) {
//        this.context = recyclerView.context
//        this.data = data
//        this.layoutRes = layoutRes
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerHolder {
//
//    }
//
//    override fun onBindViewHolder(holder: RecyclerHolder?, position: Int) {
//
//    }
//
//    override fun getItemCount() = data.size
//}