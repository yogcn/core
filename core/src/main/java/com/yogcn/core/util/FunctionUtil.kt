package com.yogcn.core.util

import android.app.Activity
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lyndon on 2017/12/13.
 */
object FunctionUtil {

    /**
     * 判断app是否安装
     * @param context
     * @param packageName 待检测app的包名
     */
    fun isApplicationInstalled(context: Context, packageName: String): Boolean {
        return try {
            var packageManager = context.packageManager
            var packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            null != packageInfo
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    /**
     *退出应用
     */
    fun exit() {
        ActivityStack.getInstance().clear()
        var pid = android.os.Process.myPid()
        android.os.Process.killProcess(pid)
    }

    /**
     * 重启应用
     * @param context
     */
    fun restartApplication(context: Context) {
        var intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        var pid = android.os.Process.myPid()
        android.os.Process.killProcess(pid)
    }

    /**
     * 安装application
     * @param context
     * @param file
     * @param fileProvider
     */
    fun installApplication(context: Context, file: File, fileProvider: String) {
        var uri = getUriFromFile(context, file, fileProvider)
        var intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            intent.flags = Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        else
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        context.startActivity(intent)
    }

    /**
     * 启动第三方应用
     * @param context
     * @param packageName 第三方应用包名
     */
    fun openOtherApplication(context: Context, packageName: String) {
        try {
            var packageManager = context.packageManager
            var intent = packageManager.getLaunchIntentForPackage(packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     *启动系统相机拍照
     * @param activity
     * @param imageName 图片全路径
     * @param requestCode 请求码，返回判断
     * @param fileProvider 7.0以后使用的文件请求provider
     */
    fun takePhoto(activity: Activity, imageName: String, requestCode: Int, fileProvider: String) {
        var imageFile = File(imageName)
        var uri = getUriFromFile(activity, imageFile, fileProvider)
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * 选择文件
     * @param activity
     * @param mime 文件类型MIME
     * @param requestCode
     */
    fun chooseFile(activity: Activity, mime: String, requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mime)
        activity.startActivityForResult(intent, requestCode)
    }


    /**
     * 获取文件uri
     * @param context
     * @param file 文件
     * @param fileProvider  7.0以后使用的文件请求provider
     */
    fun getUriFromFile(context: Context, file: File, fileProvider: String): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, fileProvider, file)
        } else {
            Uri.fromFile(file)
        }
    }

    /**
     * 调用系统拨号拨打电话
     * @param context
     * @param phone 电话号码
     */
    fun callPhone(context: Context, phone: String?) {
        if (null != phone) {
            var intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            context.startActivity(intent)
        }
    }

    /**
     * 直接拨打电话
     * @param context
     * @param phone 电话号码
     */
    fun callPhone2(context: Context, phone: String?) {
        if (null != phone) {
            var intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
            context.startActivity(intent)
        }
    }

    /**
     * 打开网页
     * @param context
     * @param url 网址
     */
    fun openWebSite(context: Context, url: String?) {
        if (null != url) {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    }

    /**
     *popupWindow显示于某控件下
     * @param popupWindow
     * @param parent 父控件
     * @param view 控件
     * @param x 基于view的横向偏移
     * @param y 基于view的纵向偏移
     */
    fun showPopupWindowDown(popupWindow: PopupWindow, parent: ViewGroup, view: View, x: Int, y: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            popupWindow.showAsDropDown(view, x, y)
        } else {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            var left = location[0] + x
            var top = location[1] + view.height + y
            popupWindow.showAtLocation(parent, Gravity.LEFT, left, top)

        }
    }

    /**
     * 随机生成文件名称
     * @param directory 文件夹路径
     * @param suffix 文件名后缀
     */
    fun genFileName(directory: String, suffix: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA)
        var random = formatter.format(Date()) + (10000 + Random().nextInt(89999)).toString()
        return directory + random + suffix
    }

    /**
     * 获取选择图片路径
     */
    fun getImagePathFromIntent(activity: Activity, data: Intent?): String? {
        var uri = data?.data
        if (uri != null) {
            var condition = arrayOf(MediaStore.Images.Media.DATA)
            var cursor = activity.contentResolver.query(uri, condition, null, null, null)
            val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            var imagePath = cursor.getString(index)
            cursor.close()
            return imagePath
        }
        return null
    }

    /**
     * 裁剪图片
     *
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    fun cropImage(activity: Activity, fromImageName: String, outputX: Int, outputY: Int, requestCode: Int, packageProvider: String) {
        val intent = Intent("com.android.camera.action.CROP")
        val uriFromFile = getUriFromFile(activity, File(fromImageName), packageProvider)
        intent.setDataAndType(uriFromFile, "image/*")
        intent.putExtra("crop", "circle")
        intent.putExtra("aspectX", 1000)
        intent.putExtra("aspectY", 1000)
        intent.putExtra("outputX", outputX)
        intent.putExtra("outputY", outputY)
        intent.putExtra("scale", true)
        intent.putExtra("return-data", true)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", false)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        activity.startActivityForResult(intent, requestCode)
    }
}