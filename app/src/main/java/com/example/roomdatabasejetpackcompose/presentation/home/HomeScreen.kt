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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.roomdatabasejetpackcompose.data.Resource
import com.example.roomdatabasejetpackcompose.navigation.Navigation
import com.example.roomdatabasejetpackcompose.presentation.home.fragments.TaskListItem
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {

    val tasks = viewModel.tasks.collectAsState(initial = Resource.Loading()).value

    val theTasks = remember {
        viewModel.myTasks
    }

    val reorderableState = rememberReorderableLazyListState(
        onMove = { from, to -> theTasks.apply { add(to.index, removeAt(from.index)) } }
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
                if (theTasks.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No tasks")
                        }
                    }
                } else {
                    items(theTasks, key = {it.id}) { task ->
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