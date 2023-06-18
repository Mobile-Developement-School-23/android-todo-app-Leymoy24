package com.example.todoapp.data.sources

import com.example.todoapp.data.models.TodoItem
import java.util.UUID

object TodoItemSource {
    private var showDone: Boolean = true
    private var completedTasks = 4
    private val todoItems: MutableList<TodoItem> = mutableListOf(
        TodoItem("Сходить в магазин", UUID.randomUUID().toString(), false, "Нет", "25 июня 2023"),
        TodoItem("Погулять с собакой", UUID.randomUUID().toString(), false, "!! Высокий", ""),
        TodoItem("Сходить в кино", UUID.randomUUID().toString(), true, "Нет", ""),
        TodoItem(
            "Бесполезная длинная заметка, зачем она нужна - непонятно. Заметка для проверки на корректность отображения элемента",
            UUID.randomUUID().toString(),
            false,
            "Нет",
            ""
        ),
        TodoItem("Взять с собой книгу", UUID.randomUUID().toString(), true, "Низкая", ""),
        TodoItem("Поездка в Казань", UUID.randomUUID().toString(), false, "!! Высокий", ""),
        TodoItem("Сдать сессию", UUID.randomUUID().toString(), false, "Нет", ""),
        TodoItem("Позвонить тете", UUID.randomUUID().toString(), false, "!! Высокий", ""),
        TodoItem("Забрать брата из детского садика", UUID.randomUUID().toString(), false, "Нет", ""),
        TodoItem("Убраться по дому", UUID.randomUUID().toString(), true, "Нет", ""),
        TodoItem("Сделать ДЗ для ШМР", UUID.randomUUID().toString(), false, "Низкая", ""),
        TodoItem("Учить Kotlin всю свою жизнь!", UUID.randomUUID().toString(), false, "Нет", ""),
        TodoItem("Решить алгосы", UUID.randomUUID().toString(), true, "Нет", ""),
    )

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

    fun getCompletedTasks(): Int {
        return completedTasks
    }

    fun getShowDone(): Boolean {
        return showDone
    }

    fun updateShowDone(newValue: Boolean) {
        showDone = newValue
    }
}