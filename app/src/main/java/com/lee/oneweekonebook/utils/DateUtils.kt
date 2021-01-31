package com.lee.oneweekonebook.utils

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @SuppressLint("SimpleDateFormat")
    fun convertLongToDateWeekString(systemTime: Long): String {
        return SimpleDateFormat("yyyy-MM-dd w'주차'")
            .format(systemTime).toString()
    }

    @SuppressLint("SimpleDateFormat")
    fun convertLongToDateString(systemTime: Long): String {
        return SimpleDateFormat("yyyy-MM-dd")
            .format(systemTime).toString()
    }

    fun formatBookPeriod(startTime: Long, endTime: Long) = run {
        "From : ${convertLongToDateString(startTime)} To : ${convertLongToDateString(endTime)}"
    }
}
