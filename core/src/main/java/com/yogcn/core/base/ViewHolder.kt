package com.yogcn.core.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yogcn.core.R
import com.yogcn.core.util.RelayoutUtil

/**
 * Created by lyndon on 2017/12/13.
 */
class ViewHolder {
    var position: Int = 0
    var dataBinding: ViewDataBinding

    companion object {
        fun get(context: Context, convertView: View?, parent: ViewGroup?, layoutRes: Int, position: Int): ViewHolder {
            return if (null == convertView) {
                ViewHolder(context, parent, layoutRes, position)
            } else {
                convertView?.getTag(R.id.bind_tag) as ViewHolder
            }
        }
    }

    /**
     * 适用于adapter中的ViewHolder
     * @param context
     * @param parent
     * @param layoutRes
     * @param position
     */
    constructor(context: Context, parent: ViewGroup?, layoutRes: Int, position: Int) {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, parent, false)
        RelayoutUtil.reLayoutViewHierarchy(dataBinding.root)
        this.position = position
        dataBinding.root.setTag(R.id.bind_tag, this)
    }

    /**
     * 获取一个普通View
     */
    constructor(context: Context, parent: ViewGroup?, layoutRes: Int, attachToRoot: Boolean) {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, parent, attachToRoot)
        RelayoutUtil.reLayoutViewHierarchy(dataBinding.root)
        dataBinding.root.setTag(R.id.bind_tag, this)
    }

}

class RecyclerHolder : RecyclerView.ViewHolder {
    var holder: ViewHolder

    constructor(holder: ViewHolder) : super(holder.dataBinding.root) {
        this.holder = holder
    }
}