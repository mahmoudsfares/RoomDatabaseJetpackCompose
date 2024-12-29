package com.example.roomdatabasejetpackcompose.presentation.add_task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.roomdatabasejetpackcompose.data.Task
import com.example.roomdatabasejetpackcompose.presentation.home.HomeViewModel

@Composable
fun AddTaskScreen(
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    var text by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
            )
            Button(
                onClick = {
                    viewModel.addTask(Task(text))
                    navHostController.popBackStack()
                },
            ) { Text("Add task") }
        }
    }
}