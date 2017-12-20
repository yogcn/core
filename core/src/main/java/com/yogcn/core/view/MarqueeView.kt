package com.yogcn.core.view

import android.content.Context
import android.os.Handler
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.util.AttributeSet
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.yogcn.core.R
import com.yogcn.core.adapter.BaseLinearLayoutAdapter
import com.yogcn.core.adapter.MarqueeAdapter

/**
 * Created by lyndon on 2017/12/20.
 */
class MarqueeView : RelativeLayout, OnPageChangeListener {

    private lateinit var viewPager: ViewPager
    var pageLayout: LinearLayout? = null
    var pageLocation = 0//默认左上角
    var pageOrientation = 0//默认横向
    var shopPage = false //默认不显示分页指示器
    var interval = 5000 //5s循环一次
    private lateinit var marqueeAdapter: MarqueeAdapter<*>
    private var pageAdapter: BaseLinearLayoutAdapter<*>? = null
    private var currentPosition: Int = 0
    private var mHandler: Handler? = null
    private var timerTask = object : Runnable {
        override fun run() {
            var currentItem = viewPager.currentItem
            currentItem++
            viewPager.currentItem = currentItem
            mHandler?.removeCallbacks(this)
            mHandler?.postDelayed(this, interval.toLong())
        }
    }

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (null != attrs) {
            var a = context.obtainStyledAttributes(attrs, R.styleable.MarqueeView)
            interval = a.getInt(R.styleable.MarqueeView_interval, 5000)
            pageLocation = a.getInt(R.styleable.MarqueeView_pageLocation, 0)
            shopPage = a.getBoolean(R.styleable.MarqueeView_showPage, false)
            pageOrientation = a.getInt(R.styleable.MarqueeView_pageOrientation, 0)
            a.recycle()
        }
        viewPager = ViewPager(context)
        viewPager.addOnPageChangeListener(this)
        addView(viewPager, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        if (shopPage) {
            pageLayout = LinearLayout(context)
            pageLayout?.orientation = if (pageOrientation == 0) LinearLayout.HORIZONTAL else LinearLayout.VERTICAL
            var layoutParams = RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            when (pageLocation) {
            //top居中
                1 -> layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
            //右上角
                2 -> layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            //左边居中
                3 -> layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
            //居中
                4 -> layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            //右边居中
                5 -> {
                    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                }
            //左下角
                6 -> layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            //底部居中
                7 -> {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
                }
            //右下角
                8 -> {
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                }
            }
            addView(pageLayout, layoutParams)
        }

    }

    /**
     * 设置切换内容适配器
     * @param adapter
     */
    fun setMarqueeAdapter(adapter: MarqueeAdapter<*>) {
        this.marqueeAdapter = adapter
        viewPager.adapter = marqueeAdapter
    }

    /**
     * 设置设置分页指示适配器
     * @param adapter
     */
    fun setPageAdapter(pageAdapter: BaseLinearLayoutAdapter<*>) {
        this.pageAdapter = pageAdapter
    }

    fun start() {
        mHandler = Handler()
        mHandler?.postDelayed(timerTask, interval.toLong())
    }

    fun stop() {
        mHandler?.removeCallbacks(timerTask)
        mHandler = null
    }

    fun onResume() {
        start()
    }

    fun onStop() {
        stop()
    }

    /**
     * 设置切换动画
     * @param transformer
     */
    fun setAnimation(transformer: ViewPager.PageTransformer) {
        viewPager.setPageTransformer(true, transformer)
    }

    /**
     * 设置切换动画
     * @param animation
     */
    override fun setAnimation(animation: Animation) {
        viewPager.animation = animation
    }


    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        this.currentPosition = position % marqueeAdapter.getDataCount()
        if (shopPage)
            pageAdapter?.selectPosition(currentPosition)
    }


}