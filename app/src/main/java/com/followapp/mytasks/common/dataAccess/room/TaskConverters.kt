package com.followapp.mytasks.common.dataAccess.room

import androidx.room.TypeConverter
import java.util.Date

class TaskConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}