package com.followapp.mytasks.common.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
//import java.time.LocalTime
import java.util.Date

@Entity(tableName = "TasksTable")
data class Task(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "is_done") var isDone: Boolean = false,
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    @ColumnInfo(name = "description") var description: String? = null,
    @ColumnInfo(name = "due_date") var dueDate: Date? = null,
)
