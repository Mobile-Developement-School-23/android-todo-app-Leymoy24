package com.example.todoapp.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.ui.adapters.diffutil.TodoDiffCallback
import com.example.todoapp.data.database.Task
import com.example.todoapp.databinding.TaskItemBinding
import com.example.todoapp.ui.fragments.ToDoFragmentDirections

class TaskAdapter(private val listener: TaskAdapterListener) : ListAdapter<Task, TaskViewHolder>(
    TodoDiffCallback()
) {

    interface TaskAdapterListener {
        fun onTaskCheckboxClicked(task: Task, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val viewHolder = TaskViewHolder.create(parent)
        viewHolder.setListener(listener)
        return viewHolder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)

        holder.infoView.setOnClickListener {
            val action = ToDoFragmentDirections.actionFragmentToDoToTaskFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }
}

class TaskViewHolder(
    private val binding: TaskItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    val infoView: ImageView = itemView.findViewById(R.id.imageView_info_task)

    private var listener: TaskAdapter.TaskAdapterListener? = null

    var task: Task? = null
        private set

    fun setListener(listener: TaskAdapter.TaskAdapterListener) {
        this.listener = listener
    }

    fun onBind(task: Task) {
        this.task = task

        views {
            checkBoxTask.text = task.taskText
            checkBoxTask.isChecked = task.isCompleted

            if (task.isCompleted) {
                checkBoxTask.paintFlags = checkBoxTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                checkBoxTask.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.label_tertiary // зачеркиваю текст, цвет серый
                    )
                )
            } else {
                checkBoxTask.paintFlags =
                    checkBoxTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                checkBoxTask.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.label_primary // возвращаю текст, цвет черный (на светлой теме)
                    )
                )
            }
            if (task.importance == "!! Высокий") {
                if (task.isCompleted) {
                    checkBoxTask.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.color_green
                        )
                    )
                } else {
                    checkBoxTask.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.color_red
                        )
                    )
                }
            } else {
                if (task.isCompleted) {
                    checkBoxTask.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.color_green
                        )
                    )
                } else {
                    checkBoxTask.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.checkbox_filter_tint
                        )
                    )
                }
            }

            checkBoxTask.setOnCheckedChangeListener(null) // Удаляем предыдущего слушателя
            checkBoxTask.setOnClickListener{
                task.isCompleted = !task.isCompleted

                if (task.isCompleted) {
                    checkBoxTask.paintFlags = checkBoxTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    checkBoxTask.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.label_tertiary // зачеркиваю текст, цвет серый
                        )
                    )
                    checkBoxTask.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.color_green
                        )
                    )
                } else {
                    checkBoxTask.paintFlags =
                        checkBoxTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    checkBoxTask.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.label_primary // возвращаю текст, цвет черный (на светлой теме)
                        )
                    )
                    if (task.importance == "!! Высокий") {
                        checkBoxTask.buttonTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.color_red
                            )
                        )
                    } else {
                        checkBoxTask.buttonTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.checkbox_filter_tint
                            )
                        )
                    }
                }

                listener?.onTaskCheckboxClicked(task, task.isCompleted)
            }
        }
    }

    private fun <T> views(block: TaskItemBinding.() -> T): T? = binding.block()

    companion object {
        fun create(parent: ViewGroup) =
            TaskItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).let(::TaskViewHolder)
    }
}