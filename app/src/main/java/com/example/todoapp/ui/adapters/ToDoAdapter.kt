package com.example.todoapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.TodoItem

class ToDoAdapter : RecyclerView.Adapter<ToDoViewHolder>() {

    private var tasks: List<TodoItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ToDoViewHolder(
            layoutInflater.inflate(
                R.layout.task_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.onBind(tasks[position])
    }

    fun setTasks(tasks: List<TodoItem>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }
}