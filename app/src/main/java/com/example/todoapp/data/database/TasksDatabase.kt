package com.example.todoapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TasksDatabase : RoomDatabase() {

    abstract val tasksDao: ToDoDao

    companion object {
        fun create(context: Context) = Room.databaseBuilder(
            context,
            TasksDatabase::class.java,
            "tasks_database"
        ).build()
    }

//    companion object {
//        @Volatile
//        private var INSTANCE: TasksDatabase? = null
//
//        fun getDatabase(context: Context): TasksDatabase {
//            val tempInstance = INSTANCE
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    TasksDatabase::class.java,
//                    "tasks_database"
//                ).build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
}