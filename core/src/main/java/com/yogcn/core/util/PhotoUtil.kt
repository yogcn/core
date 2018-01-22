package com.yogcn.core.util

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.yogcn.core.R
import com.yogcn.core.base.ViewHolder
import com.yogcn.core.databinding.PopupPhotoBinding

/**
 * Created by lyndon on 2017/12/18.
 */
class PhotoUtil : View.OnClickListener {

    private var activity: Activity? = null
    private var photoBind: PopupPhotoBinding? = null
    private var popupWindow: PopupWindow? = null
    private var imageName: String? = null
    private var directory: String? = null
    private var suffix: String? = null
    private var fileProvider: String? = null
    private var isCrop = false
    private var outputX = 0
    private var outputY = 0
    var callback: Callback? = null

    interface Callback {
        fun onPhotoSuccess(path: String?)
    }


    companion object {
        const val REQUEST_CAMERA = 0x901
        const val REQUEST_CHOOSE = 0x902
        const val REQUEST_CROP = 0x903
        fun getInstance() = Holder.instance
    }

    private object Holder {
        val instance = PhotoUtil()
    }

    /**
     * 设置图片存放路径
     * @param directory
     */
    fun setDirectory(directory: String): PhotoUtil {
        this.directory = directory
        return this
    }

    /**
     * 设置图片后缀
     * @param suffix
     */
    fun setSuffix(suffix: String): PhotoUtil {
        this.suffix = suffix
        return this
    }

    /**
     * 设置文件provider
     * @param fileProvider
     */
    fun setFileProvider(fileProvider: String): PhotoUtil {
        this.fileProvider = fileProvider
        return this
    }

    /**
     * 设置图片返回是否裁剪
     * @param isCrop 是否裁剪
     * @param outputX 宽度
     * @param outputY 高度
     */
    fun setCrop(isCrop: Boolean, outputX: Int, outputY: Int): PhotoUtil {
        this.isCrop = isCrop
        this.outputX = outputX
        this.outputX = outputY
        return this
    }

    /**
     * 初始化拍照选择框
     * @param context
     */
    private fun init(context: Activity) {
        if (null == popupWindow) {
            this.activity = context
            val holder = ViewHolder(context, null, R.layout.popup_photo, false)
            photoBind = holder.dataBinding as PopupPhotoBinding
            photoBind?.btnCamera?.setOnClickListener(this)
            photoBind?.btnPicture?.setOnClickListener(this)
            photoBind?.btnCancel?.setOnClickListener(this)
            popupWindow = PopupWindow(photoBind?.root, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            popupWindow?.isFocusable = true
            popupWindow?.isTouchable = true
            popupWindow?.isOutsideTouchable = false
            popupWindow?.setBackgroundDrawable(BitmapDrawable())
        }
    }


    /**
     *显示拍照选择
     * @param activity
     * @param parent 父容器
     */
    fun show(activity: Activity, parent: View) {
        init(activity)
        popupWindow?.showAtLocation(parent, Gravity.BOTTOM, 0, 0)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_camera -> {
                imageName = FunctionUtil.genFileName(directory!!, suffix!!)
                FunctionUtil.takePhoto(activity!!, imageName!!, REQUEST_CAMERA, fileProvider!!)
            }
            R.id.btn_picture -> {
                FunctionUtil.chooseFile(activity!!, "image/*", REQUEST_CHOOSE)
            }
        }
        popupWindow?.dismiss()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    if (!isCrop)
                        callback?.onPhotoSuccess(imageName)
                    else
                        FunctionUtil.cropImage(activity!!, imageName!!, outputX, outputY, REQUEST_CROP, fileProvider!!)
                }
                REQUEST_CHOOSE -> {
                    val imagePath = FunctionUtil.getImagePathFromIntent(activity!!, data)
                    if (!isCrop)
                        callback?.onPhotoSuccess(imagePath)
                    else {
                        imageName = FunctionUtil.genFileName(directory!!, suffix!!)
                        FunctionUtil.cropImage(activity!!, imageName!!, outputX, outputY, REQUEST_CROP, fileProvider!!)
                    }
                }
                REQUEST_CROP -> {
                    var bitmap = data?.extras?.getParcelable<Bitmap>("data")
                    BitmapUtil.saveBitmap(bitmap, imageName!!)
                    callback?.onPhotoSuccess(imageName)
                }
            }
        }
    }

    fun onDestroy() {
        callback = null
        photoBind = null
        popupWindow = null
        activity = null
        imageName = null
        isCrop = false
        outputX = 0
        outputY = 0
    }


}