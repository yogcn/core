package com.yogcn.core.base

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.yogcn.core.util.Log

/**
 * Created by lyndon on 2017/12/12.
 */
open class BaseApplication : Application() {
    var context: Context? = null
    var versionCode: Int = 1
    var versionName: String = "1.0"

    companion object {
        lateinit var instance: BaseApplication
    }


    override fun onCreate() {
        super.onCreate()
        this.context = this
        instance = this

        initAppInfo()
    }

    private fun initAppInfo() {
        try {
            var packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            versionCode = packageInfo.versionCode
            versionName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Log.w("getApplicationMeta", e.message)
        }
    }

    /**
     * 是否处于Debug模式
     */
    fun isDebug() = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    fun <T> getApplicationMeta(key: String): T? {
        return try {
            applicationInfo.metaData.get(key) as T
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Log.w("getApplicationMeta", e.message)
            null
        }
    }

}