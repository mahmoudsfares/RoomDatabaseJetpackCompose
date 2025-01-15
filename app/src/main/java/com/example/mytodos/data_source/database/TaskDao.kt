package com.example.mytodos.data_source.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mytodos.data.Task
import kotlinx.coroutines.flow.Flow

// TODO 3: create table dao, which defines the sql methods that will be used with its corresponding entity
@Dao
interface TaskDao {

    @Query("SELECT COUNT(*) FROM TaskTable")
    suspend fun getTaskCount(): Int

    @Query("SELECT * FROM TaskTable ORDER BY rank ASC")
    fun getAllTasks(): Flow<List<Task>?>

    @Insert
    suspend fun insertTask(task: Task)

    // transaction: non-abstract methods in the dao that usually call other abstract method, used for data manipulation along with mere CRUD methods
    @Transaction
    suspend fun insertTaskWithRank(task: Task) {
        val currentCount = getTaskCount()
        task.rank = currentCount + 1
        insertTask(task)
    }

    @Update
    suspend fun updateTasks(tasks: List<Task>)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM TaskTable")
    suspend fun clear()
}