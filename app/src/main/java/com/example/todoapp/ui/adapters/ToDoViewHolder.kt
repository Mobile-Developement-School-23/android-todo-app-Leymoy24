package com.example.todoapp.ui.adapters

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.TodoItem

class ToDoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox_task)
    fun onBind(todoItem: TodoItem) {
        checkBox.text = todoItem.taskText
    }
}