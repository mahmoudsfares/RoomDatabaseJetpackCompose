package com.example.roomdatabasejetpackcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roomdatabasejetpackcompose.presentation.home.HomeScreen
import com.example.roomdatabasejetpackcompose.presentation.add_task.AddTaskScreen

private object RouteNames {
    const val HOME = "home"
    const val ADD_TASK = "addTask"
}

sealed class Navigation {
    data object HomeScreen: Navigation() {
        fun getRoute(): String {
            return RouteNames.HOME
        }
    }
    data object AddTaskScreen: Navigation() {
        fun getRoute(): String {
            return RouteNames.ADD_TASK
        }
    }
}

@Composable
fun NavGraph() {
    val navHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = Navigation.HomeScreen.getRoute()) {
        composable(route = RouteNames.HOME) {
            HomeScreen(navHostController = navHostController)
        }
        composable(route = RouteNames.ADD_TASK) {
            AddTaskScreen(navHostController = navHostController)
        }
    }
}