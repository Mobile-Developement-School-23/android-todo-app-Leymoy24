package com.example.todoapp.data.repositories

import android.content.Context
import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.data.sources.TodoItemSource

class TodoItemRepository(private val dataSource: TodoItemSource) {
    fun addTask(taskText: String) {
        dataSource.addTask(taskText)
    }

    fun getTasks(): List<TodoItem> {
        return dataSource.getTasks()
    }
}