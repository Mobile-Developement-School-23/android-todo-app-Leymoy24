package com.example.todoapp

import android.content.Context

class TasksRepository {
    fun getTasks(context: Context): List<TaskInfo> {
        return buildList {
            val taskText = "Hello! I'm Ivan!"

            val numberOfTasks = 20
            for (i in 0 until numberOfTasks) add(TaskInfo(taskText))
        }
    }
}