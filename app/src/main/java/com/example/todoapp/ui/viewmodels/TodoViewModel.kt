package com.example.todoapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.data.repositories.TodoItemRepository
import com.example.todoapp.data.sources.TodoItemSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoViewModel : ViewModel() {
    private val taskRepository: TodoItemRepository
    private val _taskList: MutableLiveData<List<TodoItem>> = MutableLiveData()
    val taskList: LiveData<List<TodoItem>> = _taskList

    init {
        val dataSource = TodoItemSource()
        taskRepository = TodoItemRepository(dataSource)
    }

    fun addTask(taskText: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskRepository.addTask(taskText)
                val updatedTasks = taskRepository.getTasks()
                _taskList.postValue(updatedTasks)
            }
        }
    }
}