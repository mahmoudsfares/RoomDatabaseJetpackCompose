package com.example.mytodos.data_source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mytodos.data.Task

// TODO 4: create app database, introducing the involved entities and the dao getters
@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}