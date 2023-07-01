package com.example.todoapp.ui.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.data.database.Task
import com.example.todoapp.data.models.Product
import com.example.todoapp.data.retrofit.TaskApi
import com.example.todoapp.databinding.FragmentTaskBinding
import com.example.todoapp.ui.viewmodels.TaskViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone


class TaskFragment : Fragment() {

    lateinit var mContext: Context
    private lateinit var mTaskViewModel: TaskViewModel

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<TaskFragmentArgs>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

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

        if (args.currentTask != null) {
            binding.editTextEnterTask.setText(args.currentTask!!.taskText)
            binding.textViewImportanceText.text = args.currentTask!!.importance
            binding.textViewDate.text = args.currentTask!!.deadline
            if (args.currentTask!!.importance == "!! Высокий") {
                binding.textViewImportanceText.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.color_red
                    )
                )
            }
            if (binding.textViewDate.text != "") { binding.switchCompat.isChecked = true }
        }

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        binding.textViewDownload.setOnClickListener {
            if (args.currentTask == null){
                insertDataToDatabase()
            } else {
                updateTask()
            }
        }

        binding.imageButtonClose.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_fragmentToDo)
        }

        deleteViewChanges()

        binding.DeleteTextView.setOnClickListener {
            val taskText = binding.editTextEnterTask.text.toString().trim()
            if (taskText.isNotEmpty() && args.currentTask != null) {
                mTaskViewModel.delete(args.currentTask!!)
            }
            findNavController().navigate(R.id.action_taskFragment_to_fragmentToDo)
        }


        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Выберите дату")
        val picker: MaterialDatePicker<*> = builder.build()

        binding.switchCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
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
            } else {
                binding.textViewDate.text = ""
            }
        }


        binding.linearLayoutImportance.setOnClickListener {
            showImportanceList(binding.textViewImportanceTitle)
        }

    }

    private fun insertDataToDatabase() {
        val text = binding.editTextEnterTask.text.toString()
        val importance = binding.textViewImportanceText.text.toString()
        val deadline = binding.textViewDate.text.toString()

        if (inputCheck(text)) {
            // создаем новый таск
            val task = Task(0, text, false, importance, deadline)
            mTaskViewModel.add(task)
            Toast.makeText(requireContext(), "Заметка добавлена!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_taskFragment_to_fragmentToDo)
        } else {
            Toast.makeText(requireContext(), "Пожалуйста, введите текст заметки.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(text: String): Boolean {
        return !TextUtils.isEmpty(text)
    }

    private fun updateTask() {

        val taskText = binding.editTextEnterTask.text.toString()
        val taskImportance = binding.textViewImportanceText.text.toString()
        val taskDeadline = binding.textViewDate.text.toString()

        if (inputCheck(taskText)) {
            // создание обновленного таска
            val updatedTask = Task(args.currentTask!!.id, taskText, args.currentTask!!.isCompleted, taskImportance, taskDeadline)
            // обновление базы данных
            mTaskViewModel.update(updatedTask)
            Toast.makeText(requireContext(), "Заметка обновлена!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_taskFragment_to_fragmentToDo)
        } else {
            Toast.makeText(requireContext(), "Пожалуйста, введите текст заметки.", Toast.LENGTH_LONG).show()
        }
    }

    private fun showImportanceList(view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.popupmenu, popupMenu.menu)

        val highImportance = popupMenu.menu.getItem(2)
        val spannable = SpannableString(highImportance.title.toString())
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
            } else if (menuItem.itemId == R.id.low) {
                binding.textViewImportanceText.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.label_disable
                    )
                )
            } else {
                binding.textViewImportanceText.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.label_disable
                    )
                )
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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

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

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun <T> views(block: FragmentTaskBinding.() -> T): T? = _binding?.block()
}