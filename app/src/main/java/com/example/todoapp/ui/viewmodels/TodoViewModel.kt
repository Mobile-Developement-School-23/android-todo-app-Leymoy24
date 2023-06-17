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
import java.util.UUID

class TodoViewModel() : ViewModel() {
    private val taskRepository: TodoItemRepository
    private val _taskList: MutableLiveData<List<TodoItem>> = MutableLiveData()
    val taskList: LiveData<List<TodoItem>> = _taskList

    init {
        val dataSource = TodoItemSource

        // Получение начальных данных из TodoDataSource
        val initialTasks = dataSource.getTasks()
        _taskList.value = initialTasks

        taskRepository = TodoItemRepository(dataSource)
    }

    // Генерация уникального идентификатора
    private fun generateTaskId(): String {
        return UUID.randomUUID().toString()
    }

    fun addTask(taskText: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val taskId = generateTaskId()
                taskRepository.addTask(taskText, taskId)
                val updatedTasks = taskRepository.getTasks()
                _taskList.postValue(updatedTasks)
            }
        }
    }

    fun addTask(newTaskText: String, taskImportance: String, taskDeadline: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val taskId = generateTaskId()
                taskRepository.addTask(newTaskText, taskId, taskImportance, taskDeadline)
                val updatedTasks = taskRepository.getTasks()
                _taskList.postValue(updatedTasks)
            }
        }
    }

    fun updateTask(taskId: String, newTaskText: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskRepository.updateTask(taskId, newTaskText)
                val updatedTasks = taskRepository.getTasks()
                _taskList.postValue(updatedTasks)
            }
        }
    }

    fun updateTaskImportance(taskId: String, importance: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskRepository.updateTaskImportance(taskId, importance)
            }
        }
    }

    fun updateDeadline(taskId: String, newDate: String)
    {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskRepository.updateDeadline(taskId, newDate)
                val updatedTasks = taskRepository.getTasks()
                _taskList.postValue(updatedTasks)
            }
        }
    }

//    fun updateTask(todoItem: TodoItem) {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                taskRepository.updateTask(todoItem)
//                val updatedTasks = taskRepository.getTasks()
//                _taskList.postValue(updatedTasks)
//            }
//        }
//    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskRepository.deleteTask(taskId)
                val updatedTasks = taskRepository.getTasks()
                _taskList.postValue(updatedTasks)
            }
        }
    }

    fun getTaskById(taskId: String): TodoItem? {
        return taskRepository.getTaskById(taskId)
    }

    fun getTasks(): List<TodoItem> {
        return taskRepository.getTasks()
    }
}