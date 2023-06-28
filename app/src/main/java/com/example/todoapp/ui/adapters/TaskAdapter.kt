package com.example.todoapp.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.TodoDiffCallback
import com.example.todoapp.data.database.Task
import com.example.todoapp.databinding.FragmentToDoBinding
import com.example.todoapp.databinding.TaskItemBinding
import com.example.todoapp.ui.fragments.TaskFragmentDirections
import com.example.todoapp.ui.fragments.ToDoFragment
import com.example.todoapp.ui.fragments.ToDoFragmentDirections

class TaskAdapter(private val listener: TaskAdapterListener) : ListAdapter<Task, TaskViewHolder>(TodoDiffCallback()) {

    interface TaskAdapterListener {
        fun onTaskCheckboxClicked(task: Task, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val viewHolder = TaskViewHolder.create(parent)
        viewHolder.setListener(listener)
        return viewHolder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.onBind(getItem(position))
        val currentItem = getItem(position)

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
                        R.color.label_tertiary
                    )
                )
            } else {
                checkBoxTask.paintFlags =
                    checkBoxTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                checkBoxTask.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.label_primary
                    )
                )
                if (task.importance == "!! Высокий") {
                    checkBoxTask.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.color_red
                        )
                    )
                }
            }

            checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
                task.isCompleted = isChecked
                if (isChecked) {
                    checkBoxTask.paintFlags = checkBoxTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    checkBoxTask.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.label_tertiary
                        )
                    )
//                    task.isCompleted = true
                    checkBoxTask.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.color_green
                        )
                    )
                    //onItemClickListener.onCompletedTaskCountChanged(1)
                } else {
                    checkBoxTask.paintFlags =
                        checkBoxTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    checkBoxTask.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.label_primary
                        )
                    )
//                    task.isCompleted = false
                    checkBoxTask.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.support_separator
                        )
                    )
                    if (task.importance == "!! Высокий") {
                        checkBoxTask.buttonTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.color_red
                            )
                        )
                    }
                    //onItemClickListener.onCompletedTaskCountChanged(-1)
                }
                listener?.onTaskCheckboxClicked(task, isChecked)
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

//class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
//
//    private var taskList = emptyList<Task>()
//
//    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        private val checkBoxTask: CheckBox = itemView.findViewById(R.id.checkBox_task)
//        val infoView: ImageView = itemView.findViewById(R.id.imageView_info_task)
//
//        fun onBind(task: Task) {
//            checkBoxTask.text = task.taskText
//            checkBoxTask.isChecked = task.isCompleted
//
//            if (task.isCompleted) {
//                checkBoxTask.paintFlags = checkBoxTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//                checkBoxTask.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_tertiary))
//            } else {
//                checkBoxTask.paintFlags = checkBoxTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//                checkBoxTask.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_primary))
//                if (task.importance == "!! Высокий") {
//                    checkBoxTask.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.color_red))
//                }
//            }
//
//            checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
//                task.isCompleted = isChecked
//                if (isChecked) {
//                    checkBoxTask.paintFlags = checkBoxTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//                    checkBoxTask.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_tertiary))
////                    task.isCompleted = true
//                    checkBoxTask.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.color_green))
//                    //onItemClickListener.onCompletedTaskCountChanged(1)
//                } else {
//                    checkBoxTask.paintFlags = checkBoxTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//                    checkBoxTask.setTextColor(ContextCompat.getColor(itemView.context, R.color.label_primary))
////                    task.isCompleted = false
//                    checkBoxTask.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.support_separator))
//                    if (task.importance == "!! Высокий") {
//                        checkBoxTask.buttonTintList= ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.color_red))
//                    }
//                    //onItemClickListener.onCompletedTaskCountChanged(-1)
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
//        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false))
//    }
//
//    override fun getItemCount(): Int {
//        return taskList.size
//    }
//
//    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
//        val currentItem = taskList[position]
//        holder.onBind(currentItem)
//
//        holder.infoView.setOnClickListener {
//            val action = ToDoFragmentDirections.actionFragmentToDoToTaskFragment(currentItem)
//            holder.itemView.findNavController().navigate(action)
//        }
//    }
//
//    fun setData(tasks: List<Task>) {
//        this.taskList = tasks
//        notifyDataSetChanged()
//    }
//}