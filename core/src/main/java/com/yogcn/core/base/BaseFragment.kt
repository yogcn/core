package com.yogcn.core.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yogcn.core.util.RelayoutUtil

/**
 * Created by lyndon on 2017/12/13.
 */
abstract class BaseFragment : Fragment() {
    var title: CharSequence? = null
    private var rootLayoutResID = 0
    protected var activity: BaseActivity? = null
    protected var inflater: LayoutInflater? = null
    protected var rootDataBinding: ViewDataBinding? = null
    protected var prepared: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreate()
    }

    fun setTitle(title: String): BaseFragment {
        this.title = title
        return this
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.activity = context as BaseActivity
        inflater = LayoutInflater.from(context)
    }

    fun setContentView(layoutResID: Int) {
        this.rootLayoutResID = layoutResID
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (null == rootDataBinding) {
            rootDataBinding = DataBindingUtil.inflate(inflater!!, rootLayoutResID, container, false)
            RelayoutUtil.reLayoutViewHierarchy(rootDataBinding?.root)
            initUI()
            prepared = true
        }
        return rootDataBinding?.root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            onVisible()
        } else {
            onInVisible()
        }
    }

    override fun onResume() {
        super.onResume()
        if (userVisibleHint && prepared) {
            onVisible()
        }
    }

    override fun onPause() {
        onInVisible()
        super.onPause()

    }


    abstract fun onVisible()

    abstract fun onInVisible()

    abstract fun initUI()

    abstract fun onCreate()
    /**
     * 初始化model
     */
    open fun initModel() {}

    /**
     * 初始化数据
     */
    open fun initData() {}

    /**
     * 销毁model
     */
    open fun destroyModel() {}
}