package com.yogcn.core.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

/**
 * Created by lyndon on 2017/12/12.
 */
class JsonUtil {

    private var gson: Gson = GsonBuilder().serializeNulls().create()

    companion object {
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = JsonUtil()
    }

    /**
     * @param t 序列化对象
     * @return json序列化字符串
     */
    fun <T> toJson(t: T) = gson.toJson(t)

    /**
     * @param json json字符串
     * @param clazz 转换的对象
     * @return 转换的对象
     */
    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return gson.fromJson(json, clazz)
    }

    /**
     * @param json json字符串
     * @param type 转换的对象类型
     * @return 转换的对象
     */
    fun <T> fromJson(json: String, type: Type): T {
        return gson.fromJson(json, type)
    }


}