package com.example.todoapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.repositories.TodoItemRepository
import com.example.todoapp.ui.adapters.ToDoAdapter
import com.example.todoapp.databinding.FragmentToDoBinding
import com.example.todoapp.ui.viewmodels.TodoViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout


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

//        val toDoAdapter = ToDoAdapter()
//        val layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        binding.recyclerViewTasks.adapter = toDoAdapter
//        binding.recyclerViewTasks.layoutManager = layoutManager
//        toDoAdapter.tasks = todoItemRepository.getTasks(requireContext())

        recyclerView = binding.recyclerViewTasks
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ToDoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        taskViewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)
        taskViewModel.taskList.observe(viewLifecycleOwner) { tasks ->
            adapter.setTasks(tasks)
        }

        binding.floatingActionButtonAddTask.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragmentToDo_to_taskFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}