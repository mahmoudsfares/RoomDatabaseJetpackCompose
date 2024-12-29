package com.example.roomdatabasejetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.roomdatabasejetpackcompose.navigation.NavGraph
import com.example.roomdatabasejetpackcompose.ui.theme.RoomDatabaseJetpackComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomDatabaseJetpackComposeTheme {
                NavGraph()
            }
        }
    }
}