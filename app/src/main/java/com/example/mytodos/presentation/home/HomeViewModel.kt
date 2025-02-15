package com.example.mytodos.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodos.data.Resource
import com.example.mytodos.data.Task
import com.example.mytodos.data_source.database.AppDatabase
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

    // TODO 7: in the viewmodel, read from the database.. receive the data in a flow
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
        .catch {
            Resource.Error<List<Task>>("error occurred")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading()
        )

    // TODO 8: implement the rest of the needed operations
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