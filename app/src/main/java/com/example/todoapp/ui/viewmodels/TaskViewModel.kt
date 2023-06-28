package com.example.todoapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.database.Task
import com.example.todoapp.data.repositories.DatabaseRepository
import com.example.todoapp.locateLazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class TaskViewModel: ViewModel() {

    private val repository: DatabaseRepository by locateLazy()

    val tasks = repository.getAllData().asLiveDataFlow()

    fun add(task: Task) {
        viewModelScope.launch { repository.add(task) }
    }

    fun delete(task: Task) {
        viewModelScope.launch { repository.delete(task) }
    }

    fun update(task: Task) {
        viewModelScope.launch { repository.update(task) }
    }

    fun getCountCompleted(): LiveData<Int> { return repository.getCountCompleted() }

    fun <T> Flow<T>.asLiveDataFlow() = shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)
}

//class TaskViewModel(application: Application) : AndroidViewModel(application) {
//
//    val getAllData: LiveData<List<Task>>
//    private val repository: DatabaseRepository
//
//    init {
//        val taskDao = TasksDatabase.getDatabase(application).tasksDao()
//        repository = DatabaseRepository(taskDao)
//        getAllData = repository.getAllData
//    }
//
//    fun add(task: Task) {
//        viewModelScope.launch(Dispatchers.IO) { repository.add(task) }
//    }
//
//    fun update(task: Task) {
//        viewModelScope.launch(Dispatchers.IO) { repository.update(task) }
//    }
//
//    fun delete(task: Task) {
//        viewModelScope.launch(Dispatchers.IO) { repository.delete(task) }
//    }
//
//    fun getCountCompleted(): LiveData<Int> {
//        return repository.getCountCompleted()
//    }
//}
