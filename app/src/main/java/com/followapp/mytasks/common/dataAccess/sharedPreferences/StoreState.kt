package com.followapp.mytasks.common.dataAccess.sharedPreferences

import android.content.Context

class StoreState(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)

//    private fun getTasksFromState(): MutableList<Task>? {
//        val tasks = mutableListOf<Task>()
//
//        val size = sharedPreferences.getInt("size", 0)
//        return if (size > 0) {
//            for (i in 0 until size) {
//                val title= sharedPreferences.getString("title_$i", "").toString()
//                val description = sharedPreferences.getString("description_$i", "").toString()
//                val dueDate = sharedPreferences.getString("dueDate_$i", "").toString()
//                val timeRequired = sharedPreferences.getString("timeRequired_$i", "").toString()
//                val labels = sharedPreferences.getStringSet("labels_$i", emptySet())?.toMutableSet() ?: mutableSetOf()
//                val isDone = sharedPreferences.getBoolean("isDone_$i", false)
//                tasks.add(Task(title, isDone, description, dueDate, timeRequired, labels, ))
//            }
//            tasks
//        } else {
//            null
//        }
//    }

//    private fun saveTasksToState(tasks: List<Task>) {
//        val editor = sharedPreferences.edit()
//        editor.putInt("size", tasks.size)
//        for ( i in tasks.indices) {
//            editor.putString("title_$i", tasks[i].title)
//            editor.putString("description_$i", tasks[i].description)
//            editor.putString("dueDate_$i", tasks[i].dueDate)
//            editor.putString("timeRequired_$i", tasks[i].timeRequired)
//            editor.putStringSet("labels_$i", tasks[i].labels)
//            editor.putBoolean("isDone_$i", tasks[i].isDone)
//        }
//        editor.apply()
//    }
}

