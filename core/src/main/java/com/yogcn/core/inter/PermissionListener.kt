package com.yogcn.core.inter

/**
 * Created by lyndon on 2017/10/30.
 */
interface PermissionListener {
    /**
     * 授权成功
     */
    fun permissionGranted()

    /**
     * 授权失败
     */
    fun permissionDenied(deniedPermissions: Array<String>)
}