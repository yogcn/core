package com.yogcn.core.base

import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yogcn.core.inter.PermissionListener
import com.yogcn.core.util.ActivityStack
import com.yogcn.core.util.RelayoutUtil

/**
 * Created by lyndon on 2017/12/12.
 */
abstract class BaseActivity : AppCompatActivity(), PermissionListener {

    protected lateinit var rootDataBind: ViewDataBinding
    protected lateinit var childDataBind: ViewDataBinding

    companion object {
        const val REQUEST_PERMISSION = 0x001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStack.getInstance().put(this)
        LayoutInflater.from(this)
        getExtra()
    }

    /**
     * 页面间参数获取
     */
    fun getExtra() {

    }

    /**
     * 检验权限
     * @param permissions 需要检测的权限
     */
    fun checkPermission(permissions: Array<String>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val list = permissions.filter { PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this, it) }
            if (list.isEmpty()) {
                permissionGranted()
            } else {
                requestPermissions(list.toTypedArray(), REQUEST_PERMISSION)
            }
        } else {
            permissionGranted()
        }
    }


    /**
     * 授权回调
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION) {
            val list = permissions.filter { PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this, it) }
            if (list.isEmpty()) {
                permissionGranted()
            } else {
                permissionDenied(list.toTypedArray())
            }
        }
    }

    /**
     * 复写布局控制
     */
    override fun setContentView(layoutResID: Int) {
        rootDataBind = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false)
        setContentView(rootDataBind.root)
    }

    /**
     * 复写布局控制
     */
    override fun setContentView(view: View?) {
        RelayoutUtil.reLayoutViewHierarchy(view)
        super.setContentView(view)
    }

    /**
     * 复写布局控制
     */
    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        RelayoutUtil.reLayoutViewHierarchy(view)
        RelayoutUtil.reLayoutLayoutParams(params)
        super.setContentView(view, params)
    }

    abstract fun setChildView(layoutResID: Int)

    fun setChildView(layoutResID: Int, viewGroup: ViewGroup?) {
        childDataBind = DataBindingUtil.inflate(layoutInflater, layoutResID, null, false)
        setChildContentView(childDataBind.root, viewGroup)
    }

    fun setChildContentView(view: View?, viewGroup: ViewGroup?) {
        RelayoutUtil.reLayoutViewHierarchy(view)
        viewGroup?.addView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    /**
     * 界面可见时初始化model
     * 初始化model中注意重复添加
     */
    override fun onResume() {
        initModel()
        super.onResume()
    }
    /**
     * 界面不可见时销毁model
     */
    override fun onPause() {
        destroyModel()
        super.onPause()
    }

    override fun onDestroy() {
        ActivityStack.getInstance().remove(this)
        super.onDestroy()
    }

    /**
     * 初始化model
     */
    fun initModel() {

    }

    /**
     * 销毁model
     */
    fun destroyModel() {

    }


}