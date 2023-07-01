package com.example.todoapp.data.models

import java.util.Date

data class TodoItem(
    val id: String,
    val text: String,
    val importance: String,
    val deadline: Date?,
    val done: Boolean,
    val createdAt: Date,
    var changedAt: Date?
)
