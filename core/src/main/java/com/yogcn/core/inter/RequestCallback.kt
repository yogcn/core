package com.yogcn.core.inter

/**
 * Created by lyndon on 2017/11/2.
 */
interface RequestCallback {

    /**
     * 请求成功
     * @param tag
     * @param message
     * @param result
     */
    fun requestSuccess(tag: String, message: String, vararg result: Any)

    /**
     * 请求失败
     * @param tag
     * @param message
     */
    fun requestFailure(tag: String, message: String)


}