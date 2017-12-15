package com.yogcn.core.util

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

/**
 * Created by lyndon on 2017/12/14.
 */
object MD5Util {
    private val hexDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")

    private fun byteArrayToHexString(byteArray: ByteArray): String {
        var buffer = StringBuffer()
        byteArray.forEach {
            buffer.append(byteToHexString(it))
        }
        return buffer.toString()
    }

    private fun byteToHexString(b: Byte): String {
        var n = b.toInt()
        if (n < 0)
            n += 256
        val d1 = n / 16
        val d2 = n % 16
        return hexDigits[d1] + hexDigits[d2]
    }

    fun MD5Encode(origin: String, charSet: String): String {
        var resultString = ""
        try {
            resultString = String(origin.toByteArray())
            val md = MessageDigest.getInstance("MD5")
            resultString = byteArrayToHexString(md.digest(resultString.toByteArray(charset(charSet))))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultString
    }

    fun MD5Encode(origin: String): String {
        return MD5Encode(origin, "UTF-8")
    }

    fun getMD5Encode(file: File): String? {
        var resultString: String? = null
        try {
            val inputStream = FileInputStream(file)
            val md = MessageDigest.getInstance("MD5")
            val buffer = ByteArray(512)
            loop@ while (true) {
                var length = inputStream.read(buffer)
                when (length) {
                    -1 -> break@loop
                    else -> md.update(buffer, 0, length)
                }
            }
            val b = md.digest()
            resultString = byteArrayToHexString(b).toUpperCase()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultString
    }

}