package com.yogcn.core.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by lyndon on 2017/12/14.
 */
class Cache {

    private var sharedPreferences: SharedPreferences? = null

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = Cache()
    }

    /**
     * 获取缓存信息
     * @param context
     * @param cacheName 缓存名称
     */
    fun getCache(context: Context, cacheName: String): Cache {
        sharedPreferences = context.getSharedPreferences(cacheName, Context.MODE_PRIVATE)
        return this
    }

    /**
     * 设置缓存信息
     * @param key
     * @param value
     */
    fun <T> put(key: String, value: T) {
        var editor = sharedPreferences?.edit()
        when (value) {
            is Boolean -> editor?.putBoolean(key, value)
            is String -> editor?.putString(key, value)
            is Int -> editor?.putInt(key, value)
            is Float -> editor?.putFloat(key, value)
            is Long -> editor?.putLong(key, value)
            is MutableSet<*> -> editor?.putStringSet(key, value as MutableSet<String>)
        }
        editor?.apply()
    }

    /**
     * 获取缓存信息
     * @param key
     * @param defaultValue
     */
    fun <T> get(key: String, defaultValue: T): T {
        var obj = sharedPreferences!!.all[key]
        return when (obj) {
            null -> defaultValue
            else -> obj as T
        }
    }

    /**
     * 清空缓存
     */
    fun clear() {
        sharedPreferences?.edit()?.clear()?.apply()
    }

    /**
     * 移除指定缓存
     */
    fun remove(key: String) {
        sharedPreferences?.edit()?.remove(key)?.apply()
    }


}