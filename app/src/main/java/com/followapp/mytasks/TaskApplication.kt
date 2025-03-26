package com.followapp.mytasks

import android.app.Application
import androidx.room.Room
import com.followapp.mytasks.common.dataAccess.room.TaskDatabase
import com.followapp.mytasks.detailModule.di.detailModule
import com.followapp.mytasks.homeModule.di.homeModule
import com.google.android.gms.ads.MobileAds
import org.koin.core.context.startKoin

class TaskApplication : Application() {
    companion object {
        lateinit var database: TaskDatabase
    }

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this)

        database = Room
            .databaseBuilder(this, TaskDatabase::class.java, "TaskDatabase")
            .build()

        startKoin {
            modules(homeModule, detailModule)
        }
    }
}