package com.followapp.mytasks

import java.io.Serializable
import java.time.LocalTime
import java.util.Date

// Serializable is to use intent.putExtra to pass a task object to another activity
data class Task(
    var title: String,
    var isDone: Boolean,
    var description: String? = null,
    var dueDate: Date? = null,
    var timeRequired: LocalTime? = null,
    var labels: MutableSet<String>? = null
): Serializable
