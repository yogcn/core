package com.yogcn.demo.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.yogcn.core.base.ViewHolder
import com.yogcn.core.util.FileUtil
import com.yogcn.core.util.FunctionUtil
import com.yogcn.core.util.PhotoUtil
import com.yogcn.core.view.PullToRefreshView
import com.yogcn.demo.R
import com.yogcn.demo.adapter.StringAdapter
import com.yogcn.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), PullToRefreshView.PullToRefresh {


    private lateinit var mainBind: ActivityMainBinding
    private lateinit var data: ArrayList<String>
    private lateinit var adapter: StringAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBind = DataBindingUtil.inflate(layoutInflater, R.layout.activity_main, null, false);
        setContentView(mainBind.root)

        data = ArrayList()
        for (i in 0 until 20)
            data.add("第 $i 个item")
        adapter = StringAdapter(mainBind.refreshView.recyclerView, data, R.layout.item_string)
        mainBind.refreshView.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mainBind.refreshView.recyclerView.adapter = adapter
        mainBind.refreshView.loadMoreHolder = ViewHolder(this, mainBind.refreshView.recyclerView, R.layout.load_more, false)
        mainBind.refreshView.refreshListener = this

        FileUtil.creatDirectory(Cst.root)
        Handler().postDelayed({
            PhotoUtil.getInstance().setDirectory(Cst.root).setSuffix(Cst.IMAGE_SUFFIX).setFileProvider(Cst.FILEPROVIDER).show(this@MainActivity, mainBind.root)
        }, 2000)
    }

    class Cst {
        companion object {
            val root = Environment.getRootDirectory().path + "/demo/"
            val IMAGE_SUFFIX = ".jpg"
            val FILEPROVIDER = "com.yogcn.app.fileprovicer"

        }
    }

    override fun downRefresh() {
        Handler().postDelayed({
            data.clear()
            adapter.notifyDataSetChanged()
            for (i in 0 until 20)
                data.add("刷新 $i 个item")
            mainBind.refreshView.onRefreshFinish()
        }, 2000)

    }

    override fun upLoadMore() {
        Handler().postDelayed({
            for (i in data.size until data.size + 20)
                data.add("新增 $i 个item")
            adapter.notifyDataSetChanged()
            mainBind.refreshView.onLoadFinish()
        }, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        PhotoUtil.getInstance().onActivityResult(requestCode, resultCode, data)
    }

}
