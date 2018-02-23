package com.yogcn.core.view

import android.content.Context
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.ViewGroup
import com.yogcn.core.adapter.BaseRecycleAdapter
import com.yogcn.core.base.ViewHolder

/**
 * Created by lyndon on 2017/12/14.
 */
class PullToRefreshView : SwipeRefreshLayout {

    lateinit var recyclerView: RecyclerView

    var refreshListener: PullToRefresh? = null

    var lastVisibleItemPosition = 0

    var loadMoreHolder: ViewHolder? = null

    var isLoading = false

    var onScrollListener: RecyclerView.OnScrollListener? = null

    interface PullToRefresh {
        fun downRefresh()
        fun upLoadMore()
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        this.setOnRefreshListener {
            refreshListener?.downRefresh()
        }
        recyclerView = RecyclerView(context)
        this.addView(recyclerView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView?.layoutManager
                when (layoutManager) {
                    is LinearLayoutManager -> {
                        lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    }
                    is StaggeredGridLayoutManager -> {
                        var array = IntArray(layoutManager.spanCount)
                        layoutManager?.findLastVisibleItemPositions(array)
                        array.forEach {
                            if (it > lastVisibleItemPosition)
                                lastVisibleItemPosition = it
                        }
                    }
                }
                onScrollListener?.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!isLoading) {
                    var adapter = recyclerView?.adapter as BaseRecycleAdapter<*>
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == adapter?.itemCount) {
                        if (null != loadMoreHolder) {
                            if (adapter.footerHolder.size() == 0)
                                adapter.addFooter(loadMoreHolder)
                            adapter.notifyItemInserted(adapter.itemCount)
                        }
                        refreshListener?.upLoadMore()
                        isLoading = true
                    }
                }
                onScrollListener?.onScrollStateChanged(recyclerView, newState)
            }
        })

    }

    /**
     * 刷新完成
     */
    fun onRefreshFinish() {
        this.isRefreshing = false
    }

    /**
     * 加载更多完成
     */
    fun onLoadFinish() {
        Handler().postDelayed({
            var adapter = recyclerView?.adapter as BaseRecycleAdapter<*>
            if (null != loadMoreHolder && adapter?.footerHolder.size() > 0) {
                adapter.footerHolder.clear()
                adapter.notifyItemRemoved(adapter.itemCount)
            }
            isLoading = false
        }, 500)
    }

}