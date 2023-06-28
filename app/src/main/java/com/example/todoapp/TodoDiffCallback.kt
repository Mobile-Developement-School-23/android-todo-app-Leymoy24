package com.example.todoapp

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.data.database.Task

class TodoDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
}