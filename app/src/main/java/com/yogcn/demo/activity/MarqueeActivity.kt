package com.yogcn.demo.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.seventc.zhongjunchuang.adapter.AdvertisementAdapter
import com.yogcn.demo.R
import com.yogcn.demo.adapter.PageIconAdapter
import com.yogcn.demo.databinding.ActMarqueeBinding

/**
 * Created by lyndon on 2017/12/20.
 */
class MarqueeActivity : AppCompatActivity() {
    private lateinit var advertisementData: ArrayList<String>
    private lateinit var pageIconData: ArrayList<Int>
    private lateinit var advertisementAdapter: AdvertisementAdapter
    private lateinit var pageIconAdapter: PageIconAdapter

    private lateinit var mainBind: ActMarqueeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBind = DataBindingUtil.inflate(layoutInflater, R.layout.act_marquee, null, false)
        setContentView(mainBind.root)


        advertisementData = ArrayList()


        advertisementAdapter = AdvertisementAdapter(this, advertisementData, R.layout.item_advertisement)
        mainBind.marqueeAdvertisement.setMarqueeAdapter(advertisementAdapter)
        pageIconData = ArrayList()
        pageIconAdapter = PageIconAdapter(this, mainBind.marqueeAdvertisement.pageLayout!!, pageIconData, R.layout.item_page_icon)
        mainBind.marqueeAdvertisement.setPageAdapter(pageIconAdapter)

        advertisementData.add("http://pic149.nipic.com/file/20171216/18549423_163024285000_2.jpg")
        advertisementData.add("http://pic149.nipic.com/file/20171219/18549423_125022284000_2.jpg")
        advertisementData.add("http://pic149.nipic.com/file/20171217/18549423_175348402000_2.jpg")
        advertisementData.add("http://pic149.nipic.com/file/20171217/18549423_114153982000_2.jpg")
        (0 until advertisementData.size).forEach {
            pageIconData.add(it + 1)
        }
        advertisementAdapter.notifyDataSetChanged()
        pageIconAdapter.notifyDataSetChanged()
        mainBind.marqueeAdvertisement.start()
    }
}