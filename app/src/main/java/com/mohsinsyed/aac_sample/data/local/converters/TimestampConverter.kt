package com.mohsinsyed.aac_sample.data.local.converters

import androidx.room.TypeConverter
import java.util.*

object TimestampConverter {
    @TypeConverter
    fun fromTimestamp(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}