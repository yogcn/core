package com.seventc.zhongjunchuang.adapter

import android.content.Context
import com.yogcn.core.BR
import com.yogcn.core.adapter.MarqueeAdapter
import com.yogcn.core.base.ViewHolder

/**
 * Created by lyndon on 2017/12/20.
 */
class AdvertisementAdapter : MarqueeAdapter<String> {

    constructor(context: Context, data: MutableCollection<String>, layoutRes: Int) : super(context, data, layoutRes)

    override fun bindData(holder: ViewHolder?, t: String?, position: Int) {
        holder?.dataBinding?.setVariable(BR.url, t)
    }

}