package com.example.todoapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.databinding.FragmentTaskBinding
import com.example.todoapp.ui.viewmodels.TodoViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone


class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TodoViewModel
    private lateinit var taskId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)


        taskId = TaskFragmentArgs.fromBundle(requireArguments()).taskId

        if (taskId.isNotEmpty()) {
            val todoItem = taskViewModel.getTaskById(taskId)
            todoItem?.let {
                binding.editTextEnterTask.setText(todoItem.taskText)
                binding.textViewImportanceText.text = todoItem.importance
                if (todoItem.importance == "!! Высокий") {
                    binding.textViewImportanceText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.color_red
                        )
                    )
                }
                binding.textViewDate.text = taskViewModel.getTaskById(taskId)!!.deadline
                if (binding.textViewDate.text != "") {
                    binding.switchCompat.isChecked = true
                }
            }
        }

        binding.textViewDownload.setOnClickListener {
            val taskText = binding.editTextEnterTask.text.toString().trim()
            val taskImportance = binding.textViewImportanceText.text.toString().trim()
            val taskDeadline = binding.textViewDate.text.toString().trim()

            if (taskText.isNotEmpty()) {
                if (taskId.isNotEmpty() && taskId != "defaultTaskId") {
                    taskViewModel.updateTask(taskId, taskText)
                } else {
                    taskViewModel.addTask(taskText, taskImportance, taskDeadline)
                }
            }

            if (taskId != "defaultTaskId" && binding.textViewDate.text != taskViewModel.getTaskById(
                    taskId
                )?.deadline
            ) {
                taskViewModel.updateDeadline(taskId, binding.textViewDate.text.toString())
            }

            if (taskId != "defaultTaskId" && binding.textViewDate.text != taskViewModel.getTaskById(
                    taskId
                )?.deadline
            ) {
                taskViewModel.updateTaskImportance(
                    taskId,
                    binding.textViewImportanceText.text.toString()
                )
            }

            findNavController().navigate(R.id.action_taskFragment_to_fragmentToDo)
        }

        binding.imageButtonClose.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_fragmentToDo)
        }

        deleteViewChanges()

        binding.DeleteTextView.setOnClickListener {
            val taskText = binding.editTextEnterTask.text.toString().trim()
            if (taskText.isNotEmpty() && taskId != "defaultTaskId" && taskId.isNotEmpty()) {
                taskViewModel.deleteTask(taskId)
            }
            findNavController().navigate(R.id.action_taskFragment_to_fragmentToDo)
        }


        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
//        builder.setTheme(R.style.DatePickerStyle)
        binding.switchCompat.setOnCheckedChangeListener { _, _ ->
            builder.setTitleText("Выберите дату")
            val picker: MaterialDatePicker<*> = builder.build()
            fragmentManager?.let { manager ->
                picker.show(manager, picker.toString())
            }

            picker.addOnPositiveButtonClickListener {
                val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                utc.timeInMillis = it as Long
                val format = SimpleDateFormat("d MMMM yyyy")
                val formatted: String = format.format(utc.time)
                binding.textViewDate.text = formatted
            }
        }

        binding.linearLayoutImportance.setOnClickListener {
            showImportanceList(binding.textViewImportanceTitle)
        }

    }

    private fun showImportanceList(view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.popupmenu, popupMenu.menu)

        val todoItem: TodoItem? = taskViewModel.getTaskById(taskId)

        val highImportance = popupMenu.menu.getItem(2)
        val spannable: SpannableString = SpannableString(highImportance.title.toString())
        spannable.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    view.context,
                    R.color.color_red
                )
            ), 0, spannable.length, 0
        )
        highImportance.title = spannable

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem: MenuItem? ->
            binding.textViewImportanceText.text = menuItem?.title
            if (menuItem!!.itemId == R.id.high) {
                binding.textViewImportanceText.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.color_red
                    )
                )
                todoItem?.let {
                    val importance = getString(R.string.high)
                    taskViewModel.updateTaskImportance(it.taskId, importance)
                }
            } else if (menuItem.itemId == R.id.low) {
                binding.textViewImportanceText.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.label_disable
                    )
                )
                todoItem?.let {
                    val importance = getString(R.string.low)
                    taskViewModel.updateTaskImportance(it.taskId, importance)
                }
            } else {
                binding.textViewImportanceText.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.label_disable
                    )
                )
                todoItem?.let {
                    val importance = getString(R.string.no)
                    taskViewModel.updateTaskImportance(it.taskId, importance)
                }
            }
            true
        })
        popupMenu.show()
    }

    private fun deleteViewChanges() {
        val editText = binding.editTextEnterTask
        val textView = binding.DeleteTextView
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.delete)?.mutate()

        // Проверяем, содержит ли EditText текст изначально
        if (editText.text.isNotEmpty()) {
            val color = ContextCompat.getColor(requireContext(), R.color.color_red)
            drawable?.setTint(color)
            textView.setTextColor(color)
        } else {
            val color = ContextCompat.getColor(requireContext(), R.color.label_disable)
            drawable?.setTint(color)
            textView.setTextColor(color)
        }

        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

        // Следим за изменениями текста в EditText
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // метод не используется!
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    val color = ContextCompat.getColor(requireContext(), R.color.label_disable)
                    drawable?.setTint(color)
                    textView.setTextColor(color)
                } else {
                    val color = ContextCompat.getColor(requireContext(), R.color.color_red)
                    drawable?.setTint(color)
                    textView.setTextColor(color)
                }
                textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            }

            override fun afterTextChanged(s: Editable?) {
                // метод не используется!
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}