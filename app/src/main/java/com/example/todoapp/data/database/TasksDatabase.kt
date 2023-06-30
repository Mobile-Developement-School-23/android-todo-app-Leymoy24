package com.example.todoapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {

    abstract val tasksDao: TaskDao

    companion object {
        fun create(context: Context) = Room.databaseBuilder(
            context,
            TasksDatabase::class.java,
            "tasks_database"
        ).build()
    }
}