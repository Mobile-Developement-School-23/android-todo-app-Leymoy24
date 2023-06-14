package com.example.todoapp.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTaskBinding
import com.example.todoapp.ui.viewmodels.TodoViewModel
import java.util.Calendar


class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

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

        val taskViewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)

        val taskId = TaskFragmentArgs.fromBundle(requireArguments()).taskId

        if (taskId.isNotEmpty()) {
            // Получение элемента по идентификатору taskId из ViewModel
            val todoItemText = taskViewModel.getTaskById(taskId)

            // Проверка на null перед использованием элемента
            todoItemText?.let {
                binding.editTextEnterTask.setText(todoItemText)
            }
        }

        binding.textViewDownload.setOnClickListener {
            val taskText = binding.editTextEnterTask.text.toString().trim()

            if (taskText.isNotEmpty()) {
                if (taskId.isNotEmpty() && taskId != "defaultTaskId") {
                    taskViewModel.updateTask(taskId, taskText)
                } else {
                    taskViewModel.addTask(taskText)
                }
                binding.editTextEnterTask.text.clear()
            }
            findNavController().navigate(R.id.action_taskFragment_to_fragmentToDo)
        }

        // datePicker не доработан!!!
        binding.switchCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, year, monthOfYear, dayOfMonth ->
                        // Обработка выбранной даты
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(year, monthOfYear, dayOfMonth)
                        // Дальнейшие действия с выбранной датой
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )

                datePickerDialog.show()
            } else {
                // ...
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}