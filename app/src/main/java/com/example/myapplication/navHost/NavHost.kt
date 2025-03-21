package com.example.myapplication.navHost

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.view.FolderScreen
import com.example.myapplication.view.NewNoteScreen
import com.example.myapplication.view.NoteScreen
import com.example.myapplication.view.ViewNoteScreen
import com.example.myapplication.viewModel.FolderViewModel
import com.example.myapplication.viewModel.NoteViewModel
import com.example.myapplication.viewModel.ReminderViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteApp() {
    val navController = rememberNavController();
    val noteModel: NoteViewModel = viewModel();
    val folderViewModel: FolderViewModel = viewModel();
    val reminderViewModel: ReminderViewModel = viewModel();
    NavHost(navController = navController, startDestination = "noteScreen") {
        composable("noteScreen") {
            NoteScreen(navController = navController, noteViewModel = noteModel,folderViewModel = folderViewModel)
        }
        composable("newNoteScreen") {
            NewNoteScreen(navController = navController, noteModel = noteModel,folderViewModel = folderViewModel)
        }
        composable("note_detail") {
            ViewNoteScreen(navController = navController, noteModel = noteModel , reminderViewModel= reminderViewModel)
        }
        composable ("folder") {
            FolderScreen(navController=navController, noteModel= noteModel, folderViewModel = folderViewModel)
        }
    }
}
