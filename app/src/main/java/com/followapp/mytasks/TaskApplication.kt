package com.followapp.mytasks

import android.app.Application
import androidx.room.Room
import com.followapp.mytasks.common.dataAccess.room.TaskDatabase

class TaskApplication: Application() {
    companion object {
        lateinit var database: TaskDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this,
            TaskDatabase::class.java,
            "WineDatabase")
            .build()
    }
}