package com.yogcn.core.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lyndon on 2017/12/14.
 */
object DateUtil {
    /**
     * 格式化日期
     * @param date 日期
     * @param pattern 转换格式
     */
    fun dateFormat(date: Date?, pattern: String): String? {
        return if (null == date) null else SimpleDateFormat(pattern).format(date)
    }

    /**
     * 获取当前年
     */
    fun getCurrentYear(): String? {
        return dateFormat(Date(), "yyyy")
    }

    /**
     * 将秒转换成日期
     * @param second 秒
     * @param pattern 转换格式
     */
    fun formatSecond(second: Long, pattern: String): String? {
        return dateFormat(longToDate(second), pattern)
    }

    /**
     * 将秒转换成日期
     */
    fun longToDate(second: Long): Date? {
        var instance = Calendar.getInstance()
        instance.timeInMillis = second * 1000
        return instance.time
    }

    /**
     * 字符串转换日期
     * @param dateStr 日期字符串
     * @param pattern 转换格式
     */
    fun stringToDate(dateStr: String?, pattern: String): Date? {
        return if (null == dateStr) null else SimpleDateFormat(pattern).parse(dateStr)
    }


}