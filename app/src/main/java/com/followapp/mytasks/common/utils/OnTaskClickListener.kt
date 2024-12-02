package com.followapp.mytasks.common.utils

import com.followapp.mytasks.common.entities.Task

interface OnTaskClickListener {
    fun onClick(task: Task)
}