package com.yogcn.core.base

import android.util.Log
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.yogcn.core.inter.RequestCallback

/**
 * Created by lyndon on 2017/11/2.
 */
abstract class BaseModel : StringCallback() {

    private var callbackList = ArrayList<RequestCallback>()

    fun addCallback(callback: RequestCallback) {
        if (!callbackList.contains(callback)) {
            Log.e("this","addCallback $callback")
            callbackList.add(callback)
        }
    }

    fun removeCallback(callback: RequestCallback) {
        if (callbackList.contains(callback)) {
            Log.e("this","removeCallback $callback")
            callbackList.remove(callback)
        }
    }

    override fun onSuccess(response: Response<String>) {
        val call = response.rawCall
        if (!call.isCanceled) {
            val tag = response.rawCall.request().tag().toString()
            val result = response.body()
            doSuccess(tag, result)
        } else {
            val url = response.rawCall.request().url().encodedPath()
            Log.e("YOGCN", "网络请求：$url 已经取消")
        }
    }

    override fun onError(response: Response<String>) {
        val call = response.rawCall
        if (!call.isCanceled) {
            val tag = response.rawCall.request().tag().toString()
            if (null == response.rawResponse) {
                dispatchError(tag, "网络连接失败")
            } else {
                val code = response.rawResponse.code()
                dispatchError(tag, "请求失败,错误码：" + code)
            }
        } else {
            val url = response.rawCall.request().url().encodedPath()
            Log.e("YOGCN", "网络请求：$url 已经取消")
        }
    }

    /**
     * 处理请求成功
     *
     * @param tag
     * @param result
     */
    protected abstract fun doSuccess(tag: String, result: String)

    /**
     * 错误提示
     *
     * @param tag
     * @param message 提示信息
     */
    protected fun dispatchError(tag: String, message: String) {
        for (callback in callbackList) {
            callback.requestFailure(tag, message)
        }
    }

    /**
     * 处理成功后
     *
     * @param tag
     * @param message 提示信息
     * @param result  返回数据
     */
    protected fun dispatchSuccess(tag: String, message: String, vararg result: Any) {
        for (callback in callbackList) {
            callback.requestSuccess(tag, message, result)
        }
    }
}