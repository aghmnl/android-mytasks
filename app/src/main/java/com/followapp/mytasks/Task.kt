package com.followapp.mytasks

import java.time.LocalTime
import java.util.Date

data class Task(
    var title: String,
    var isDone: Boolean,
    var description: String? = null,
    var dueDate: Date? = null,
    var timeRequired: LocalTime? = null,
    var labels: MutableSet<String>? = null
)
