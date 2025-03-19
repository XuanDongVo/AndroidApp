package com.example.myapplication.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.collectAsState
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
import com.example.myapplication.components.dialog.DateTimePickerDialog
import com.example.myapplication.components.dialog.MinimalDialog
import com.example.myapplication.components.noteDetail.ColorPicker
import com.example.myapplication.components.noteDetail.CustomTopAppBar
import com.example.myapplication.components.noteDetail.NoteContent
import com.example.myapplication.viewModel.ReminderViewModel
import com.example.myapplication.viewmodel.NoteViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewNoteScreen(navController: NavController, noteModel: NoteViewModel , reminderViewModel: ReminderViewModel) {
    var presses by remember { mutableIntStateOf(0) }
    val note by noteModel.selectedNote.collectAsState()
    var titleText by remember { mutableStateOf(note?.title ?: "") }
    var contentText by remember { mutableStateOf(note?.content ?: "") }
    var isReminder by remember { mutableStateOf(note?.isRemider) }
    var noteColor by remember { mutableStateOf(note?.colorBackGround?.let { Color(it) } ?: Color.White) }
    var showDialog by remember { mutableStateOf(false) } // Hiển thị menu tùy chọn
    var remindMe  by remember { mutableStateOf(false) } // Hiển thị chọn ngày giờ

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val initialTitle = note?.title ?: ""
    val initialContent = note?.content ?: ""
    val initialColor = note?.colorBackGround?.let { Color(it) } ?: Color.White

    var selectedTime by remember { mutableStateOf<Long?>(null) }
    var selectedDate by remember { mutableStateOf<String?>(null) }
    

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
                modifier = Modifier.fillMaxSize(),
                isReminder = isReminder
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
                    yOffset = 0.dp
                )
            }
        }

        if(remindMe) {
            DateTimePickerDialog(
                onDismissRequest = { remindMe = false },
                onDateTimeSelected = { timeInMillis, date ->
                    selectedTime = timeInMillis
                    selectedDate = date

                    val noteId = note?.id ?: return@DateTimePickerDialog

                    // Gọi insertReminder vào Database và lên lịch nhắc nhở
//                    LaunchedEffect(timeInMillis) {
                    coroutineScope.launch {
                        reminderViewModel.insertReminder(noteId, timeInMillis, date)
                    }
                }
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