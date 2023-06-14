package com.example.todoapp.data.repositories

import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.data.sources.TodoItemSource

class TodoItemRepository(private val dataSource: TodoItemSource) {
    fun addTask(taskText: String, taskId: String) {
        dataSource.addTask(taskText, taskId)
    }

    fun getTasks(): List<TodoItem> {
        return dataSource.getTasks()
    }

    fun getTaskById(taskId: String): String? {
        return dataSource.getTaskById(taskId)
    }

    fun updateTask(taskId: String, taskText: String) {
        val taskToUpdate = dataSource.getTaskById(taskId)
        if (taskToUpdate != null) {
            val updatedTask = TodoItem(taskText, taskId)
            dataSource.updateTask(updatedTask)
        }
    }
}