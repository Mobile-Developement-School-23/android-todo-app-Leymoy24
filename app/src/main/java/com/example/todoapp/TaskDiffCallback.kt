package com.example.todoapp

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.data.models.TodoItem

class TaskDiffCallback(private val oldTasks: List<TodoItem>, private val newTasks: List<TodoItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldTasks.size
    }

    override fun getNewListSize(): Int {
        return newTasks.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTasks[oldItemPosition].taskId == newTasks[newItemPosition].taskId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldTasks[oldItemPosition] == newTasks[newItemPosition]
    }
}