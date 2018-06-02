package com.yogcn.demo.adapter

import android.content.Context
import android.widget.LinearLayout
import com.yogcn.core.adapter.BaseLinearLayoutAdapter
import com.yogcn.core.base.ViewHolder
import com.yogcn.demo.databinding.ItemPageIconBinding

/**
 * Created by lyndon on 2017/12/20.
 */
class PageIconAdapter : BaseLinearLayoutAdapter<Int> {


    constructor(context: Context, container: LinearLayout, data: MutableCollection<Int>, layoutRes: Int) : super(context, container, data, layoutRes)

    override fun bindData(holder: ViewHolder, item: Int, position: Int) {
        var itemPageIconBind = holder.dataBinding as ItemPageIconBinding
        itemPageIconBind.root.isSelected = (position == selectPosition)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }


}
