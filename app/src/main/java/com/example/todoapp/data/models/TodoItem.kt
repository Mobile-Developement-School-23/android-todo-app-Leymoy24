package com.example.todoapp.data.models

data class TodoItem(
    var taskText: String,
    val taskId: String,
    var isCompleted: Boolean = false,
    var importance: String = "Нет",
    var deadline: String
)
