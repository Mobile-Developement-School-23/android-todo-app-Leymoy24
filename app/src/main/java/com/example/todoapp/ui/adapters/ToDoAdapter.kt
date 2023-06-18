package com.example.todoapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.TaskDiffCallback
import com.example.todoapp.data.models.TodoItem

class ToDoAdapter : RecyclerView.Adapter<ToDoViewHolder>() {

    private var tasks: List<TodoItem> = listOf()
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ToDoViewHolder(
            layoutInflater.inflate(
                R.layout.task_item,
                parent,
                false
            ), onItemClickListener!!
        )
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todoItem = tasks[position]
        holder.onBind(todoItem)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(todoItem)
        }
    }

    fun setTasks(newTasks: List<TodoItem>) {
        val diffResult = DiffUtil.calculateDiff(TaskDiffCallback(tasks, newTasks))
        tasks = newTasks
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickListener {
        fun onItemClick(todoItem: TodoItem)
        fun onCompletedTaskCountChanged(count: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }
}