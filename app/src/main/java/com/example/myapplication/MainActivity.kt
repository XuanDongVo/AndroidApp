// MainActivity.kt
package com.example.myapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.view.NewNoteScreen
import com.example.myapplication.view.NoteScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "noteScreen") {
                composable("noteScreen") {
                    NoteScreen(navController = navController)
                }
                composable("newNoteScreen") {
                    NewNoteScreen(navController = navController)
                }
            }
        }
    }
}
