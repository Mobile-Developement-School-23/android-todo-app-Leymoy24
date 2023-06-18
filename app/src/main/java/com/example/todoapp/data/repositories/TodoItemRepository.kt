package com.example.todoapp.data.repositories

import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.data.sources.TodoItemSource

class TodoItemRepository(private val dataSource: TodoItemSource) {

    fun addTask(newTaskText: String, taskId: String, taskImportance: String, taskDeadline: String) {
        dataSource.addTask(newTaskText, taskId, taskImportance, taskDeadline)
    }

    fun getTasks(): List<TodoItem> {
        return dataSource.getTasks()
    }

    fun getTaskById(taskId: String): TodoItem? {
        return dataSource.getTaskById(taskId)
    }

    fun updateTask(taskId: String, taskText: String, taskImportance: String) {
        val taskToUpdate = dataSource.getTaskById(taskId)
        if (taskToUpdate != null) {
            val updatedTask = TodoItem(
                taskText,
                taskId,
                dataSource.getFlag(taskToUpdate),
                taskImportance,
                dataSource.getDeadline(taskToUpdate)
            )
            dataSource.updateTask(updatedTask)
        }
    }

    fun deleteTask(taskId: String) {
        dataSource.deleteTask(taskId)
    }

    fun updateTaskImportance(taskId: String, importance: String) {
        val taskToUpdate = dataSource.getTaskById(taskId)
        if (taskToUpdate != null) {
            val updatedTask = TodoItem(
                dataSource.getTaskById(taskId)!!.taskText,
                taskId,
                dataSource.getFlag(taskToUpdate),
                importance,
                dataSource.getDeadline(taskToUpdate)
            )
            dataSource.updateTask(updatedTask)
        }
    }

    fun updateDeadline(taskId: String, newDate: String) {
        val taskToUpdate = dataSource.getTaskById(taskId)
        if (taskToUpdate != null) {
            val updatedTask = TodoItem(
                dataSource.getTaskById(taskId)!!.taskText,
                taskId,
                dataSource.getFlag(taskToUpdate),
                dataSource.getImportance(taskToUpdate),
                newDate
            )
            dataSource.updateTask(updatedTask)
        }
    }

    fun getCompletedTasks(): Int {
        return dataSource.getCompletedTasks()
    }
}