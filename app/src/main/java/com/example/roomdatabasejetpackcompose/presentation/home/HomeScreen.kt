package com.example.roomdatabasejetpackcompose.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.roomdatabasejetpackcompose.data.Resource
import com.example.roomdatabasejetpackcompose.data.Task
import com.example.roomdatabasejetpackcompose.navigation.Navigation
import com.example.roomdatabasejetpackcompose.presentation.home.fragments.TaskListItem
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {

    // TODO 9: in the composable, collect the data from the flow in the viewmodel as state for it to be updated with changes
    val tasks = viewModel.tasks.collectAsState().value

    // TODO 10: create a reordered tasks list to decouple ui state from database state
    // in other words, it will temporarily reflect the new order in the ui in case of drag-and-drop reordering and avoid snapping back.
    // if we directly used tasksState.data to display the tasks, the UI would "snap back" to the original order whenever the database updates.
    // using this will a) ensure smooth and responsive drag-and-drop interactions, and b) allow us to use it to update the database after the dragging ends.
    val reorderedTasks = remember { mutableStateListOf<Task>() }

    // TODO 11: THIS DOESN't WORK, when you add a task the app crashes
    LaunchedEffect(tasks) {
        if (tasks is Resource.Success && tasks.data != null) {
            reorderedTasks.addAll(tasks.data)
        } else {
            reorderedTasks.clear()
        }
    }

    val reorderableState = rememberReorderableLazyListState(
        onMove = { from, to ->
            reorderedTasks.apply {
                add(to.index, removeAt(from.index))
            }
        },
        onDragEnd = { _, _ ->
            run {
                val updatedTasks = reorderedTasks.mapIndexed { index, task ->
                    task.copy(rank = index)
                }
                viewModel.updateTasks(updatedTasks)
            }
        }
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navHostController.navigate(Navigation.AddTaskScreen.getRoute())
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = reorderableState.listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(all = 16.dp)
                .reorderable(reorderableState)
                .detectReorderAfterLongPress(reorderableState),
        ) {
            if (tasks is Resource.Loading) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (tasks is Resource.Error) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error occurred")
                    }
                }
            } else {
                if (reorderedTasks.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No tasks")
                        }
                    }
                } else {
                    items(reorderedTasks, key = { it.id }) { task ->
                        ReorderableItem(
                            reorderableState = reorderableState,
                            key = task.id
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                TaskListItem(
                                    task = task,
                                    onDeleteTask = { task -> viewModel.deleteTask(task) })
                            }
                        }
                    }
                }
            }
        }
    }
}