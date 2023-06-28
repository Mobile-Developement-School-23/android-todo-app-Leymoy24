package com.example.todoapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.database.Task
import com.example.todoapp.databinding.FragmentToDoBinding
import com.example.todoapp.ui.viewmodels.TodoViewModel
import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.ui.adapters.TaskAdapter
import com.example.todoapp.ui.viewmodels.TaskViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class ToDoFragment : Fragment(), TaskAdapter.TaskAdapterListener {

    companion object {
        fun newInstance() = ToDoFragment()
    }

    private val mTaskViewModel: TaskViewModel by viewModels()
//    private val adapter: TaskAdapter? get() = views { recyclerViewTasks.adapter as? TaskAdapter }

    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var layoutManager: LinearLayoutManager
//    private lateinit var taskViewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentToDoBinding.inflate(inflater).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views {
            collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
            collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)

            adapter = TaskAdapter(this@ToDoFragment)
            recyclerViewTasks.adapter = adapter
        }


        mTaskViewModel.tasks.onEach(::renderTasks).launchIn(lifecycleScope)



        recyclerView = binding.recyclerViewTasks
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        adapter = TaskAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        //taskViewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)

//        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java) //

//        // Отслеживание кол-ва выполненных тасков
        mTaskViewModel.getCountCompleted().observe(viewLifecycleOwner, { count ->
            binding.textViewDone.text = getString(R.string.done, count)
        })


//        mTaskViewModel.getAllData.observe(viewLifecycleOwner, Observer { tasks ->
//            adapter.setData(tasks)
//        })

//        taskViewModel.taskList.observe(viewLifecycleOwner) {
//            val currentValue = taskViewModel.getShowDone() // Получаем состояние глазика
//            if (currentValue) {
//                binding.imageButtonVisibility.setImageResource(R.drawable.visibility)
//            } else {
//                binding.imageButtonVisibility.setImageResource(R.drawable.visibility_off)
//            }
//            val tasksToShow = if (currentValue) {
//                taskViewModel.taskList.value // Показать все задачи
//            } else {
//                taskViewModel.getUncompletedTasks() // Показать только невыполненные задачи
//            }
//            adapter.setTasks(tasksToShow!!)
//        }

//        taskViewModel.completedTaskCount.observe(viewLifecycleOwner) { count ->
//            binding.textViewDone.text = getString(R.string.done, count)
//        }

//        adapter.setOnItemClickListener(object : ToDoAdapter.OnItemClickListener {
//            override fun onItemClick(todoItem: TodoItem) {
//                navigateToTaskFragment(todoItem)
//            }
//
//            override fun onCompletedTaskCountChanged(count: Int) {
//                taskViewModel.updateCompletedTaskCount(count)
//            }
//        })
//        binding.imageButtonVisibility.setOnClickListener {
//            val currentValue = taskViewModel.getShowDone() // Получаем текущее состояние глазика
//            lateinit var tasksToShow: List<TodoItem> // здесь будет итоговый список тасков, который необходимо отобразить
//            if (currentValue) { // если глазик "включен", значит, необходимо его переключить на "выкл" и отобразить только невыполненные
//                tasksToShow = taskViewModel.getUncompletedTasks()
//                binding.imageButtonVisibility.setImageResource(R.drawable.visibility_off)
//                taskViewModel.updateShowDone(!currentValue) // обновляем состояние глазика в репозитории
//            } else { // если глазик "выключен", значит, необходимо его переключить на "вкл" и отобразить все таски
//                tasksToShow = taskViewModel.taskList.value!!
//                binding.imageButtonVisibility.setImageResource(R.drawable.visibility)
//                taskViewModel.updateShowDone(!currentValue) // обновляем состояние глазика в репозитории
//            }
//            adapter.setTasks(tasksToShow) // устанавливаем в адаптер итоговый список тасков
//        }

        binding.floatingActionButtonAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentToDo_to_taskFragment)
        }
    }

//    private fun navigateToTaskFragment(todoItem: TodoItem) {
//        val action = ToDoFragmentDirections.actionFragmentToDoToTaskFragment(todoItem.taskId)
//        findNavController().navigate(action)
//    }

    private fun renderTasks(tasks: List<Task>) {
        adapter.submitList(tasks)
    }

    override fun onTaskCheckboxClicked(task: Task, isChecked: Boolean) {
        // Обработка изменения состояния чекбокса таска
        // Можно здесь обновить состояние таска в базе данных или выполнить другие необходимые действия
        val taskUpdate = Task(task.id, task.taskText, isChecked, task.importance, task.deadline)
        mTaskViewModel.update(taskUpdate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun <T> views(block: FragmentToDoBinding.() -> T): T? = _binding?.block()
}