package com.example.roomdatabasejetpackcompose.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdatabasejetpackcompose.data.Resource
import com.example.roomdatabasejetpackcompose.data.Task
import com.example.roomdatabasejetpackcompose.data_source.database.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val appDatabase: AppDatabase) : ViewModel() {

    val tasks: StateFlow<Resource<List<Task>>> = appDatabase
        .getTaskDao()
        .getAllTasks()
        .map { tasks ->
            if (tasks.isNullOrEmpty()) {
                Resource.Success(emptyList())
            } else {
                Resource.Success(tasks)
            }
        }
        .catch { e ->
            Resource.Error<List<Task>>("error occurred")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading()
        )

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.getTaskDao().insertTaskWithRank(task)
        }
    }

    fun updateTasks(tasks: List<Task>) {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.getTaskDao().updateTasks(tasks)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.getTaskDao().deleteTask(task)
        }
    }
}