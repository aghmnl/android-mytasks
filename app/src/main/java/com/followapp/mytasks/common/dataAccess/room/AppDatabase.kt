package com.followapp.mytasks.common.dataAccess.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.followapp.mytasks.common.entities.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(TaskConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDAO(): TaskDAO
}