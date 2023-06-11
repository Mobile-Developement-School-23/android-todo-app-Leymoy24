package com.example.todoapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.TaskInfo
import com.example.todoapp.ToDoViewHolder

class ToDoAdapter: RecyclerView.Adapter<ToDoViewHolder>() {

    var tasks = listOf<TaskInfo>()

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
}