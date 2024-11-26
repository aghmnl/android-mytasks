package com.followapp.mytasks.common.dataAccess.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.followapp.mytasks.common.entities.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(TaskConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDAO(): TaskDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tasks_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
