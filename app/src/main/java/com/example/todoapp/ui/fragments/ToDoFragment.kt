package com.example.todoapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.ui.adapters.ToDoAdapter
import com.example.todoapp.databinding.FragmentToDoBinding
import com.example.todoapp.ui.viewmodels.TodoViewModel
import com.example.todoapp.data.models.TodoItem


class ToDoFragment : Fragment() {

    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ToDoAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var taskViewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        binding.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

        recyclerView = binding.recyclerViewTasks
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ToDoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        taskViewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)

        taskViewModel.taskList.observe(viewLifecycleOwner) { tasks ->
            val currentValue =
                taskViewModel.getShowDone() // Получаем состояние иконки "Скрыть выполненные"
            val tasksToShow = if (currentValue) {
                taskViewModel.taskList.value // Показать все задачи
            } else {
                taskViewModel.getUncompletedTasks() // Показать только невыполненные задачи
            }
            taskViewModel.updateShowDone(!currentValue)
            adapter.setTasks(tasksToShow!!)
        }

        taskViewModel.completedTaskCount.observe(viewLifecycleOwner) { count ->
            binding.textViewDone.text = getString(R.string.done, count)
        }

        adapter.setOnItemClickListener(object : ToDoAdapter.OnItemClickListener {
            override fun onItemClick(todoItem: TodoItem) {
                navigateToTaskFragment(todoItem)
            }

            override fun onCompletedTaskCountChanged(count: Int) {
                taskViewModel.updateCompletedTaskCount(count)
            }
        })

        val resource1 = R.drawable.visibility
        val resource2 = R.drawable.visibility_off
        var currentResource = resource1
//        val currentValue = taskViewModel.getShowDone()
        binding.imageButtonVisibility.setOnClickListener {
            val currentValue = taskViewModel.getShowDone()
            // Переключаем текущий ресурс на альтернативный
            currentResource = if (currentResource == resource1) {
                resource2
            } else resource1
            binding.imageButtonVisibility.setImageResource(currentResource)

            val tasksToShow = if (currentValue) {
                taskViewModel.taskList.value // Показать все задачи
            } else {
                taskViewModel.getUncompletedTasks() // Показать только невыполненные задачи
            }
            taskViewModel.updateShowDone(!currentValue)
            adapter.setTasks(tasksToShow!!)
        }

        binding.floatingActionButtonAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentToDo_to_taskFragment)
        }
    }

    private fun navigateToTaskFragment(todoItem: TodoItem) {
        val action = ToDoFragmentDirections.actionFragmentToDoToTaskFragment(todoItem.taskId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}