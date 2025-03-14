package com.example.myapplication.navHost

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.view.NewNoteScreen
import com.example.myapplication.view.NoteScreen
import com.example.myapplication.viewmodel.NoteViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteApp() {
    val navController = rememberNavController()

    // ✅ Sử dụng NavHost từ Navigation Compose
    NavHost(navController = navController, startDestination = "noteScreen") {
        composable("noteScreen") {
            NoteScreen(navController = navController)
        }
        composable("newNoteScreen") {
            val noteViewModel: NoteViewModel = viewModel()
            NewNoteScreen(navController = navController )
        }
    }
}
