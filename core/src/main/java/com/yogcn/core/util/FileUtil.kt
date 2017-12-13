package com.yogcn.core.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by lyndon on 2017/12/13.
 */
object FileUtil {
    /**
     * 判断SD卡是否存在
     */
    fun isSdCardExit() = Environment.isExternalStorageEmulated()

    /**
     * 判断文件是否存在
     * @param path 文件路径
     */
    fun isFileExit(path: String): Boolean {
        var file = File(path)
        return file.exists()
    }

    /**
     * 创建文件夹
     * @param path 文件夹路径
     */
    fun creatDirectory(path: String): Boolean {
        return if (isFileExit(path))
            true
        else {
            var file = File(path)
            file.mkdirs()
        }
    }

    /**
     * 删除文件/文件夹
     * @param path 文件/文件夹路径
     */
    fun deleteFile(path: String) {
        deleteFile(File(path))
    }

    /**
     * 删除文件/文件夹
     * @param file 文件/文件夹
     */
    fun deleteFile(file: File) {
        if (file.exists()) {
            if (file.isDirectory) {
                file.deleteRecursively()
            } else {
                file.delete()
            }
        }
    }

    /**
     * copy Asset下文件到指定目录
     * @param context
     * @param fileName assets下文件名称
     * @param path 指定目录位置
     */
    fun copyAssets(context: Context, fileName: String, path: String) {
        try {
            var inputStream = context.assets.open(fileName)
            var destFile = File(path)
            if (!destFile.exists()) {
                var fileOutputStream = FileOutputStream(destFile, false)
                var bytes = ByteArray(1024)
                while ((inputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, bytes.size)
                }
                fileOutputStream.close()
                inputStream.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("FileUtil", e.message)
        }
    }

}