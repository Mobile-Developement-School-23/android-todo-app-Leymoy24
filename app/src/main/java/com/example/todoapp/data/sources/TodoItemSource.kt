package com.example.todoapp.data.sources

import com.example.todoapp.data.models.TodoItem
import java.util.UUID

object TodoItemSource {
    private val todoItems: MutableList<TodoItem> = mutableListOf(
        TodoItem("Сходить в магазин", UUID.randomUUID().toString(), false, "Нет", ""),
        TodoItem("Погулять с собакой", UUID.randomUUID().toString(), false, "Нет", ""),
        TodoItem("Сходить в кино", UUID.randomUUID().toString(), false, "Нет", ""),
        TodoItem(
            "Бесполезная длинная заметка, зачем она нужна - непонятно. Заметка для проверки на корректность отображения элемента",
            UUID.randomUUID().toString(),
            false,
            "Нет",
            ""
        ),
        TodoItem("Взять с собой книгу", UUID.randomUUID().toString(), false, "Нет", ""),
        TodoItem("Поездка в Казань", UUID.randomUUID().toString(), false, "Нет", ""),
        TodoItem("Сдать сессию", UUID.randomUUID().toString(), false, "Нет", ""),
        TodoItem("Позвонить тете", UUID.randomUUID().toString(), false, "Нет", ""),
        TodoItem("Забрать брата из детского садика", UUID.randomUUID().toString(), false, "Нет", ""),
        TodoItem("Убраться по дому", UUID.randomUUID().toString(), false, "Нет", ""),
    )

    fun addTask(taskText: String, taskId: String) {
        val todoItem = TodoItem(taskText, taskId, false, "Нет", "")
        todoItems.add(todoItem)
    }

    fun addTask(newTaskText: String, taskId: String, taskImportance: String, taskDeadline: String) {
        val todoItem = TodoItem(newTaskText, taskId, false, taskImportance, taskDeadline)
        todoItems.add(todoItem)
    }

    fun getTasks(): List<TodoItem> {
        return todoItems
    }

    fun getTaskById(taskId: String): TodoItem? {
        return todoItems.find { it.taskId == taskId }
    }

    fun updateTask(updatedTask: TodoItem) {
        val index = todoItems.indexOfFirst { it.taskId == updatedTask.taskId }
        if (index != -1) {
            todoItems[index] = updatedTask
        }
    }

    fun deleteTask(taskId: String) {
        todoItems.removeAll { it.taskId == taskId }
    }

    fun getFlag(todoItem: TodoItem): Boolean {
        return todoItem.isCompleted
    }

    fun getImportance(todoItem: TodoItem): String {
        return todoItem.importance
    }

    fun getDeadline(todoItem: TodoItem): String {
        return todoItem.deadline
    }

}