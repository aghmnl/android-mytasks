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
    @ColumnInfo(name = "version") var version: Int = 0 // Add this line
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false
        if (title != other.title) return false
        if (isDone != other.isDone) return false
        if (description != other.description) return false
        if (dueDate != other.dueDate) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + isDone.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (dueDate?.hashCode() ?: 0)
        result = 31 * result + version
        return result
    }
}

