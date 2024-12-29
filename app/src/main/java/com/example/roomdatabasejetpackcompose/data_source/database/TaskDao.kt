package com.example.roomdatabasejetpackcompose.data_source.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.roomdatabasejetpackcompose.data.Task
import kotlinx.coroutines.flow.Flow

// TODO 4: create table dao, which defines the sql methods that will be used with its corresponding entity
@Dao
interface TaskDao {

    @Query("SELECT COUNT(*) FROM TaskTable")
    suspend fun getTaskCount(): Int

    @Query("SELECT * FROM TaskTable ORDER BY id ASC")
    fun getAllTasks(): Flow<List<Task>?>

    @Insert
    suspend fun insertTask(task: Task)

//    @Transaction
//    suspend fun insertTaskWithRank(task: Task) {
//        val currentCount = getTaskCount()
//        task.rank = currentCount + 1
//        insertTask(task)
//    }

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM TaskTable")
    suspend fun clear()
}