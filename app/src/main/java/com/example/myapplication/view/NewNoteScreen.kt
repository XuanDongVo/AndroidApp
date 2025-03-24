package com.example.myapplication.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.noteDetail.ColorPicker
import com.example.myapplication.components.noteDetail.CustomTopAppBar
import com.example.myapplication.components.noteDetail.NoteContent
import com.example.myapplication.viewModel.FolderViewModel
import com.example.myapplication.viewModel.NoteViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteScreen(navController: NavController, noteModel: NoteViewModel ,folderViewModel : FolderViewModel) {
    var presses by remember { mutableIntStateOf(0) }
    var titleText by remember { mutableStateOf("") }
    var contentText by remember { mutableStateOf("") }
    var noteColor by remember { mutableStateOf(Color.White) }

    // Trạng thái và scope cho Bottom Sheet
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                onBackClick = {navController.popBackStack()},
                onMoreClick = {
                    coroutineScope.launch {
                        noteModel.addNote(titleText, contentText, noteColor.toArgb(),folderViewModel.folderId.value )
                        noteModel.getNotes(folderViewModel.folderId.value)
                        folderViewModel.getAllFolders()
                    }
                    navController.popBackStack()
                },
                titleHeader = "Thêm ghi chú",
                icon = Icons.Default.Check
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet = true // Hiển thị Bottom Sheet khi nhấn
                    presses++
                }
            ) {
                Icon(Icons.Default.Palette, contentDescription = "Colors")
            }
        }
    ) { innerPadding ->
        NoteContent(
            titleText = titleText,
            onTitleChange = { titleText = it },
            contentText = contentText,
            onContentChange = { contentText = it },
            backgroundColor = noteColor,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            isReminder = false // Đặt `innerPadding` vào đây
        )

        // Tích hợp Bottom Sheet trực tiếp
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Choose a color",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    ColorPicker(onColorSelected = { color ->
                        noteColor = color // Cập nhật màu khi chọn
                        coroutineScope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    })

                }
            }
        }
    }
}

