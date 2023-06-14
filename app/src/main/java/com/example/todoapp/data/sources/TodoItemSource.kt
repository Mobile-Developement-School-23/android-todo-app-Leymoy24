package com.example.todoapp.data.sources

import com.example.todoapp.data.models.TodoItem

class TodoItemSource {
    private val todoItems: MutableList<TodoItem> = mutableListOf()

    fun addTask(taskText: String, taskId: String) {
        val todoItem = TodoItem(taskText, taskId)
        todoItems.add(todoItem)
    }

    fun getTasks(): List<TodoItem> {
        return todoItems
    }

    fun getTaskById(taskId: String): String? {
        val task = todoItems.find { it.taskId == taskId }
        return task?.taskText
    }

    fun updateTask(updatedTask: TodoItem) {
        val index = todoItems.indexOfFirst { it.taskId == updatedTask.taskId }
        if (index != -1) {
            todoItems[index] = updatedTask
        }
    }
}