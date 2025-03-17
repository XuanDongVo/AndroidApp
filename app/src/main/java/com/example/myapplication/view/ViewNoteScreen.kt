package com.example.myapplication.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.dialog.DateTimePickerDialog
import com.example.myapplication.components.dialog.MinimalDialog
import com.example.myapplication.components.noteDetail.ColorPicker
import com.example.myapplication.components.noteDetail.CustomTopAppBar
import com.example.myapplication.components.noteDetail.NoteContent
import com.example.myapplication.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewNoteScreen(navController: NavController, noteModel: NoteViewModel) {
    var presses by remember { mutableIntStateOf(0) }
    val note by noteModel.selectedNote.collectAsState()
    var titleText by remember { mutableStateOf(note?.title ?: "") }
    var contentText by remember { mutableStateOf(note?.content ?: "") }
    var noteColor by remember { mutableStateOf(note?.colorBackGround?.let { Color(it) } ?: Color.White) }
    var showDialog by remember { mutableStateOf(false) } // Hiển thị menu tùy chọn
    var remindMe  by remember { mutableStateOf(false) } // Hiển thị chọn ngày giờ

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val initialTitle = note?.title ?: ""
    val initialContent = note?.content ?: ""
    val initialColor = note?.colorBackGround?.let { Color(it) } ?: Color.White

    

    val hasChanges by remember(titleText, contentText, noteColor) {
        mutableStateOf(
            titleText != initialTitle ||
                    contentText != initialContent ||
                    noteColor != initialColor
        )
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                onBackClick = { navController.popBackStack() },
                onMoreClick = {
                    if (hasChanges) {
                        coroutineScope.launch {
                            noteModel.modifyNote(note?.id ?: -1, titleText, contentText, noteColor.toArgb())
                        }
                        navController.popBackStack()
                    } else {
                        showDialog = true
                    }
                },
                titleHeader = "",
                icon = if (hasChanges) Icons.Default.Check else Icons.Default.MoreVert
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet = true
                    presses++
                }
            ) {
                Icon(Icons.Default.Palette, contentDescription = "Colors")
            }
        }
    ) { innerPadding ->
        Box( // Thay Row bằng Box để hỗ trợ chồng lấp
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NoteContent(
                titleText = titleText,
                onTitleChange = { titleText = it },
                contentText = contentText,
                onContentChange = { contentText = it },
                backgroundColor = noteColor,
                modifier = Modifier.fillMaxSize()
            )

            // Hiển thị Dialog với animation
            AnimatedVisibility(
                visible = showDialog,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                MinimalDialog(
                    onDismissRequest = { showDialog = false },
                    onRemindMe = {
                        showDialog = false
                        remindMe = true;
                    },
                    onNavigateTo = {
                        // Xử lý Chuyển tới (có thể điều hướng)
                        showDialog = false
                    },
                    onDelete = {
                        // Xử lý Xóa
                        coroutineScope.launch {
                            note?.let { noteModel.deleteNote(it) }
                            navController.popBackStack()
                        }
                        showDialog = false
                    },
                    xOffset = 550.dp, // Vị trí tùy chỉnh
                    yOffset = -10.dp
                )
            }
        }

        if(remindMe) {
            DateTimePickerDialog(
                onDismissRequest = {},
                onDateTimeSelected = {}
            )
        }

        // Tích hợp Bottom Sheet trực tiếp
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
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
                        noteColor = color
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