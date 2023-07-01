package com.example.todoapp.data.repositories

import androidx.lifecycle.LiveData
import com.example.todoapp.data.database.Task
import com.example.todoapp.data.database.TasksDatabase
import com.example.todoapp.data.retrofit.TaskApi
import com.example.todoapp.data.retrofit.TaskApiRequestElement
import com.example.todoapp.data.retrofit.TaskApiRequestList
import com.example.todoapp.data.retrofit.TaskApiResponseElement
import com.example.todoapp.data.retrofit.TaskApiResponseList
import com.example.todoapp.utils.Constants.BASE_URL
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DatabaseRepository(private val db: TasksDatabase) {

    private val dao get() = db.tasksDao



    val interceptor = HttpLoggingInterceptor()

    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)).build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create()).build()

    val taskApi = retrofit.create(TaskApi::class.java)



    fun getAllData(): Flow<List<Task>> = dao.getAllData()

    fun getCountCompleted(): LiveData<Int> { return dao.getCountCompleted() }

    suspend fun add(task: Task) = dao.add(task)

    suspend fun delete(task: Task) = dao.delete(task)

    suspend fun update(task: Task) = dao.update(task)

    sealed class Result<out T : Any> {
        data class Success<out T : Any>(val data: T) : Result<T>()
        data class Error(val exception: Exception) : Result<Nothing>()
    }

    suspend fun getListFromServer() {
        val tasks = taskApi.getList()
    }

    suspend fun addTaskToServer(token: String, revision: Int, body: TaskApiRequestElement): Result<TaskApiResponseElement> {

        return try {
            val response = taskApi.addTask(token, revision, body)
            if (response.isSuccessful) {
                val task = response.body()
                Result.Success(task!!)
            } else {
                Result.Error(Exception("Failed to add task to server"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // остальные методы с аналогичными изменениями

    suspend fun deleteTaskFromServer(token: String, id: String): Result<TaskApiResponseElement> {
        return try {
            val response = taskApi.deleteTask(token, id)
            if (response.isSuccessful) {
                val deletedTask = response.body()
                Result.Success(deletedTask!!)
            } else {
                Result.Error(Exception("Failed to delete task from server"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateTaskOnServer(token: String, id: String, body: TaskApiRequestElement): Result<TaskApiResponseElement> {
        return try {
            val response = taskApi.updateTask(token, id, body)
            if (response.isSuccessful) {
                val updatedTask = response.body()
                Result.Success(updatedTask!!)
            } else {
                Result.Error(Exception("Failed to update task on server"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateListOnServer(token: String, revision: Int, body: TaskApiRequestList): Result<TaskApiResponseList> {
        return try {
            val response = taskApi.updateList(token, revision, body)
            if (response.isSuccessful) {
                val updatedList = response.body()
                Result.Success(updatedList!!)
            } else {
                Result.Error(Exception("Failed to update list on server"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}