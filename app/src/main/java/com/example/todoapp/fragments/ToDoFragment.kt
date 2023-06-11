package com.example.todoapp.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.TasksRepository
import com.example.todoapp.adapters.ToDoAdapter
import com.example.todoapp.databinding.FragmentToDoBinding


class ToDoFragment : Fragment() {

    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    private val tasksRepository = TasksRepository()

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

        val toDoAdapter = ToDoAdapter()
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewTasks.adapter = toDoAdapter
        binding.recyclerViewTasks.layoutManager = layoutManager
        toDoAdapter.tasks = tasksRepository.getTasks(requireContext())

        binding.floatingActionButtonAddTask.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragmentToDo_to_taskFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}