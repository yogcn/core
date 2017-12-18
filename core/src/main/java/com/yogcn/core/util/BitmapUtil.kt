package com.yogcn.core.util

import android.graphics.Bitmap
import android.view.View
import java.io.File
import java.io.FileOutputStream

/**
 * Created by lyndon on 2017/12/18.
 */
object BitmapUtil {
    /**
     * 保存图片文件
     * @param bitmap
     * @param filePath
     */
    fun saveBitmap(bitmap: Bitmap?, filePath: String) {
        saveBitmap(bitmap, filePath, 100)
    }

    /**
     * 保存控件
     * @param view
     * @param filepath
     */
    fun saveBitmap(view: View, filePath: String) {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bitmap = view.drawingCache
        saveBitmap(bitmap, filePath, 100)
    }

    /**
     * 保存图片
     *
     * @param bitmap
     * @param filePath
     * @param quality
     */
    fun saveBitmap(bitmap: Bitmap?, filePath: String, quality: Int) {
        var quality = quality
        if (bitmap == null) return
        if (!(0 < quality && quality <= 100)) quality = 100

        val f = File(filePath)
        if (f.exists()) {
            f.delete()
        }
        try {
            val out = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
