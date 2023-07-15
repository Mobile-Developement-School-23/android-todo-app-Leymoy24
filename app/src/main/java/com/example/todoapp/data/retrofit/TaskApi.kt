package com.example.todoapp.data.retrofit

import androidx.room.Delete
import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.utils.Constants.TOKEN_API
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {
    @GET("list")
    @Headers("Authorization: Bearer $TOKEN_API")
    suspend fun getList(): Response<TaskApiResponseList>

    @PATCH("list")
    suspend fun updateList(
        @Header("Authorization: Bearer $TOKEN_API") token: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Body body: TaskApiRequestList
    ): Response<TaskApiResponseList>

    @GET("list/{id}")
    suspend fun getTaskById(
        @Header("Authorization: Bearer $TOKEN_API") token: String,
        @Path("id") id: String
    ): Response<TaskApiResponseElement>

    @POST("list")
    suspend fun addTask(
        @Header("Authorization: Bearer $TOKEN_API") token: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Body body: TaskApiRequestElement
    ): Response<TaskApiResponseElement>

    @PUT("list/{id}")
    suspend fun updateTask(
        @Header("Authorization: Bearer $TOKEN_API") token: String,
        @Path("id") id: String,
        @Body body: TaskApiRequestElement
    ): Response<TaskApiResponseElement>

    @DELETE("list/{id}")
    suspend fun deleteTask(
        @Header("Authorization: Bearer $TOKEN_API") token: String,
        @Path("id") id: String
    ): Response<TaskApiResponseElement>
}