package com.example.roomdatabasejetpackcompose.data_source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roomdatabasejetpackcompose.data.Task

// TODO 5: create app database, introducing the involved entities and the dao getters
@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}