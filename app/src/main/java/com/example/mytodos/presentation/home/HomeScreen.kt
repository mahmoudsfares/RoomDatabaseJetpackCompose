package com.example.mytodos.presentation.home

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
import com.example.mytodos.data.Resource
import com.example.mytodos.data.Task
import com.example.mytodos.navigation.Navigation
import com.example.mytodos.presentation.home.fragments.TaskListItem
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {

    // TODO 9: in the composable, collect the data from the flow in the viewmodel as state for it to be updated with changes
    val tasksState = viewModel.tasks.collectAsState().value

    // TODO 10: create a reordered tasks list to decouple ui state from database state
    // in other words, it will temporarily reflect the new order in the ui in case of drag-and-drop reordering and avoid snapping back.
    // if we directly used tasksState.data to display the tasks, the UI would "snap back" to the original order whenever the database updates.
    // using this will a) ensure smooth and responsive drag-and-drop interactions, and b) allow us to use it to update the database after the dragging ends.
    // remember: preserves the state during composition
    val reorderedTasks = remember { mutableStateListOf<Task>() }

    // TODO 11: sync ui State (reorderedTasks) with Database State (tasksState)
    // launchedEffect is usually used to listen to state changes
    LaunchedEffect(tasksState) {
        reorderedTasks.clear()
        if (tasksState is Resource.Success && tasksState.data != null) {
            reorderedTasks.addAll(tasksState.data)
        }
    }

    // TODO 12: create reorderable state to be passed to the lazy column and reorderable list item
    // defines the behavior for when an item is moved up or down, and for when the dragging ends
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
        // TODO 13: pass the reorderable state to the column as follows
        LazyColumn(
            state = reorderableState.listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(all = 16.dp)
                .reorderable(reorderableState)
                .detectReorderAfterLongPress(reorderableState),
        ) {
            if (tasksState is Resource.Loading) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (tasksState is Resource.Error) {
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
                    // TODO 14: pass the reorderable state to the list item as follows, pay attention to the passed keys and make sure they're unique
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