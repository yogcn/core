package com.yogcn.core.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.yogcn.core.base.BaseFragment

/**
 * Created by lyndon on 2017/12/13.
 */
class FragmentAdapter : FragmentPagerAdapter {
    private var fm: FragmentManager
    private var fragmentList: ArrayList<BaseFragment>
    var detachable = true

    constructor(fm: FragmentManager, fragmentList: ArrayList<BaseFragment>) : super(fm) {
        this.fm = fm
        this.fragmentList = fragmentList
    }

    override fun getCount() = fragmentList.size

    override fun getItem(position: Int) = fragmentList[position]

    override fun getPageTitle(position: Int) = getItem(position).title

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        var item = super.instantiateItem(container, position) as BaseFragment
        fm.beginTransaction().show(item).commit()
        return item
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        if (detachable) {
            super.destroyItem(container, position, `object`)
        } else
            fm.beginTransaction().hide(`object` as BaseFragment).commit()
    }
}