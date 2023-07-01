package com.example.todoapp.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.data.database.Task
import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.data.retrofit.TaskApiRequestElement
import com.example.todoapp.databinding.FragmentToDoBinding
import com.example.todoapp.ui.adapters.TaskAdapter
import com.example.todoapp.ui.viewmodels.TaskViewModel
import com.example.todoapp.utils.Constants.TOKEN_API
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class ToDoFragment : Fragment(), TaskAdapter.TaskAdapterListener {
    companion object {
        fun newInstance() = ToDoFragment()
    }

    var flagOfVisibilityButton: Boolean = true
    private val mTaskViewModel: TaskViewModel by viewModels()

    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TaskAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // проверка работоспособности сервера
        mTaskViewModel.getListFromServer()
        return FragmentToDoBinding.inflate(inflater).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views {
            collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
            collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerViewTasks.layoutManager = layoutManager

            adapter = TaskAdapter(this@ToDoFragment)
            recyclerViewTasks.adapter = adapter
        }

        mTaskViewModel.tasks.onEach(::renderTasks).launchIn(lifecycleScope)

        // Отслеживание кол-ва выполненных тасков
        mTaskViewModel.getCountCompleted().observe(viewLifecycleOwner, { count ->
            binding.textViewDone.text = getString(R.string.done, count)
        })

        mTaskViewModel.changeMode()

        binding.imageButtonVisibility.setOnClickListener {
            flagOfVisibilityButton = mTaskViewModel.changeVisibilityButton(flagOfVisibilityButton)
            if (flagOfVisibilityButton) {
                binding.imageButtonVisibility.setImageResource(R.drawable.visibility)
            } else {
                binding.imageButtonVisibility.setImageResource(R.drawable.visibility_off)
            }
            mTaskViewModel.changeMode()
        }

        binding.floatingActionButtonAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentToDo_to_taskFragment)
        }



        binding.swipeRefreshLayout.setOnRefreshListener {
            // Выполняем загрузку данных
            // ...
            Handler().postDelayed({
                binding.swipeRefreshLayout.isRefreshing = false
            }, 1000)
        }

    }

    private fun renderTasks(tasks: List<Task>) {
        if (mTaskViewModel.showAll) {
            adapter.submitList(tasks)
        } else {
            adapter.submitList(tasks.filter { !it.isCompleted })
        }
    }

    override fun onTaskCheckboxClicked(task: Task, isChecked: Boolean) {
        val taskUpdate = Task(task.id, task.taskText, isChecked, task.importance, task.deadline)
        mTaskViewModel.update(taskUpdate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun <T> views(block: FragmentToDoBinding.() -> T): T? = _binding?.block()
}