package com.followapp.mytasks

import android.app.Application
import androidx.room.Room
import com.followapp.mytasks.common.dataAccess.room.TaskDatabase
import com.google.android.gms.ads.MobileAds

class TaskApplication: Application() {
    companion object {
        lateinit var database: TaskDatabase
    }

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)

        database = Room.databaseBuilder(this,
            TaskDatabase::class.java,
            "TaskDatabase")
            .build()
    }
}