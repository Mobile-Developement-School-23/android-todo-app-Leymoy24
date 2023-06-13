package com.example.todoapp.data.sources

import com.example.todoapp.data.models.TodoItem

class TodoItemSource {
    private val todoItems: MutableList<TodoItem> = mutableListOf()

    fun addTask(taskText: String) {
        val todoItem = TodoItem(taskText)
        todoItems.add(todoItem)
    }

    fun getTasks(): List<TodoItem> {
        return todoItems
    }
}