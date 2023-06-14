package com.example.todoapp.ui.fragments

import android.graphics.Color
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
import com.google.android.material.appbar.CollapsingToolbarLayout
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

        binding.floatingActionButtonAddTask.setColorFilter(Color.argb(255, 255, 255, 255));

        val mCollapsingToolbarLayout: CollapsingToolbarLayout = binding.collapsingToolbar
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

        recyclerView = binding.recyclerViewTasks
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ToDoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        taskViewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)
        taskViewModel.taskList.observe(viewLifecycleOwner) { tasks ->
            adapter.setTasks(tasks)
        }

        adapter.setOnItemClickListener(object : ToDoAdapter.OnItemClickListener {
            override fun onItemClick(todoItem: TodoItem) {
                navigateToTaskFragment(todoItem)
            }
        })

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