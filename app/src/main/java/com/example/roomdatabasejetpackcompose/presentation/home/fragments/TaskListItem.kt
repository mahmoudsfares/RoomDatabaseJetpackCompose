package com.example.roomdatabasejetpackcompose.presentation.home.fragments

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.roomdatabasejetpackcompose.data.Task

@Composable
fun TaskListItem(task: Task, onDeleteTask: (Task) -> Unit){
    Card (modifier = Modifier.fillMaxWidth()){
        Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)){
            Text(task.name, modifier = Modifier.weight(1f))
            IconButton(onClick = { onDeleteTask(task) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete task",
                    tint = Color.Red
                )
            }
        }
    }
}