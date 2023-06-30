package com.example.todoapp.data.repositories

import androidx.lifecycle.LiveData
import com.example.todoapp.data.database.Task
import com.example.todoapp.data.database.TasksDatabase
import kotlinx.coroutines.flow.Flow

class DatabaseRepository(private val db: TasksDatabase) {

    private val dao get() = db.tasksDao

    fun getAllData(): Flow<List<Task>> = dao.getAllData()

    fun getCountCompleted(): LiveData<Int> { return dao.getCountCompleted() }

    suspend fun add(task: Task) = dao.add(task)

    suspend fun delete(task: Task) = dao.delete(task)

    suspend fun update(task: Task) = dao.update(task)

}