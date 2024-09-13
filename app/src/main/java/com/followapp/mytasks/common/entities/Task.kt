package com.followapp.mytasks.common.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//import java.time.LocalTime
import java.util.Date

@Entity
data class Task(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "is_done") var isDone: Boolean,
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "description") var description: String? = null,
    @ColumnInfo(name = "due_date") var dueDate: Date? = null,
//    @ColumnInfo(name = "time_required") var timeRequired: LocalTime? = null,
//    @ColumnInfo(name = "labels") var labels: String? = null  // Room doesn't support MutableSet, so we'll store labels as a comma-separated string
)
