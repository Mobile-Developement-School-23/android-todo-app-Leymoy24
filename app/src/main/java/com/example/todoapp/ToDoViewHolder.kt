package com.example.todoapp

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class ToDoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox_task)
    fun onBind(taskInfo: TaskInfo) {
        checkBox.text = taskInfo.taskText
    }
}