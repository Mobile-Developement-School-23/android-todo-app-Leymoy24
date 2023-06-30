package com.example.todoapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.database.Task
import com.example.todoapp.data.repositories.DatabaseRepository
import com.example.todoapp.locateLazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val repository: DatabaseRepository by locateLazy()

    var job : Job? = null

    private val _tasks = MutableSharedFlow<List<Task>>()
    val tasks: SharedFlow<List<Task>> = _tasks.asSharedFlow()

    var showAll = true

    init {
        loadTasks()
    }

    fun changeMode() {
        job?.cancel()
        showAll = !showAll
        loadTasks()
    }

    fun changeVisibilityButton(flag: Boolean): Boolean { return !flag }

    private fun loadTasks() {
        job = viewModelScope.launch(Dispatchers.IO) {
            _tasks.emitAll(repository.getAllData())
        }
    }

    fun add(task: Task) {
        viewModelScope.launch(Dispatchers.IO) { repository.add(task) }
    }

    fun delete(task: Task) {
        viewModelScope.launch(Dispatchers.IO) { repository.delete(task) }
    }

    fun update(task: Task) {
        viewModelScope.launch(Dispatchers.IO) { repository.update(task) }
    }

    fun getCountCompleted(): LiveData<Int> {
        return repository.getCountCompleted()
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
