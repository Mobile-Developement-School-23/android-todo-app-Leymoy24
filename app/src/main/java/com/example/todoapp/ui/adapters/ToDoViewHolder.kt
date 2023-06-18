package com.example.todoapp.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.TodoItem

class ToDoViewHolder(itemView: View, private val onItemClickListener: ToDoAdapter.OnItemClickListener): RecyclerView.ViewHolder(itemView) {

    private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox_task)

    fun onBind(todoItem: TodoItem) {
        checkBox.text = todoItem.taskText
        checkBox.isChecked = todoItem.isCompleted

        if (todoItem.isCompleted) {
            checkBox.paintFlags = checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            checkBox.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_tertiary))
        } else {
            checkBox.paintFlags = checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            checkBox.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_primary))
            if (todoItem.importance == "!! Высокий") {
                checkBox.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.color_red))
            }
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBox.paintFlags = checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                checkBox.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_tertiary))
                todoItem.isCompleted = true
                checkBox.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.color_green))
                onItemClickListener.onCompletedTaskCountChanged(1)
            } else {
                checkBox.paintFlags = checkBox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                checkBox.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_primary))
                todoItem.isCompleted = false
                checkBox.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.support_separator))
                if (todoItem.importance == "!! Высокий") {
                    checkBox.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.color_red))
                }
                onItemClickListener.onCompletedTaskCountChanged(-1)
            }
            todoItem.isCompleted = isChecked
        }
    }
}