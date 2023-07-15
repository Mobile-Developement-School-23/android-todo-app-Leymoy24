package com.example.todoapp.data.retrofit

import com.example.todoapp.data.models.TodoItem
import com.google.gson.annotations.SerializedName
import java.util.Date

data class TaskApiResponseList(
    @SerializedName("status")
    val status: String,
    @SerializedName("revision")
    val revision: Int,
    @SerializedName("list")
    val list: List<ToDoItemResponseRequest>
)

data class TaskApiRequestList(
    @SerializedName("status")
    val status: String,
    @SerializedName("list")
    val list: List<ToDoItemResponseRequest>
)

data class TaskApiResponseElement(
    @SerializedName("revision")
    val revision: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("element")
    val element: ToDoItemResponseRequest
)

data class TaskApiRequestElement(
    @SerializedName("element")
    val element: ToDoItemResponseRequest
)

data class ToDoItemResponseRequest(
    @SerializedName("id")
    val id: String,

    @SerializedName("text")
    val text: String,

    @SerializedName("deadline")
    val deadline: Long?,

    @SerializedName("done")
    val done: Boolean,

    @SerializedName("color")
    val color: String?,

    @SerializedName("importance")
    val importance: String,

    @SerializedName("created_at")
    val created_at: Long,

    @SerializedName("changed_at")
    val changed_at: Long,

    @SerializedName("last_updated_by")
    val last_updated_by: String
) {
    fun toToDoItem(): TodoItem = TodoItem(
        id,
        text,
        importance,
        deadline?.let { Date(it) },
        done,
        Date(created_at),
        Date(changed_at)
    )
    companion object {
        fun fromToDoTask(toDoItem: TodoItem, deviseId: String): ToDoItemResponseRequest {
            return ToDoItemResponseRequest(
                id = toDoItem.id,
                text = toDoItem.text,
                importance = toDoItem.importance,
                deadline = toDoItem.deadline?.time,
                done = toDoItem.done,
                created_at = toDoItem.createdAt.time,
                changed_at = toDoItem.changedAt?.time ?: 0,
                last_updated_by = deviseId,
                color = null
            )
        }
    }
}