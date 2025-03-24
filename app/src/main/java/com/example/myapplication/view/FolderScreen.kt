package com.example.myapplication.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.components.folder.AddFolderBottomSheetContent
import com.example.myapplication.components.folder.FolderItem
import com.example.myapplication.viewModel.FolderViewModel
import com.example.myapplication.viewModel.NoteViewModel
import kotlinx.coroutines.launch
import  com.example.myapplication.components.dialog.DeleteConfirmationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(
    navController: NavController,
    noteModel: NoteViewModel,
    folderViewModel: FolderViewModel,
    shouldPopBackAfterSelection: Boolean = false
) {
    val selectedFolder by folderViewModel.selectedFolder.collectAsState()
    val folderData by folderViewModel.folderList.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // State cho Bottom Sheet
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Thư mục",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        folderViewModel.folderId.value?.let {
                            if (it > 0) {
                                showDeleteDialog = true
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Xóa thư mục"
                        )
                    }
                }
            )
        }
    )
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Danh sách thư mục
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(folderData.keys.toList()) { folder ->
                    val noteCount = folderData[folder] ?: 0
                    FolderItem(
                        folderName = folder,
                        noteCount = noteCount as Int,
                        isSelected = selectedFolder == folder,
                        onClick = {
                            coroutineScope.launch {
                                folderViewModel.selectFolder(folder)
                                if (shouldPopBackAfterSelection) {
                                    val note = noteModel.selectedNote.value
                                    if (note != null) {
                                        noteModel.updateNoteInFolder(
                                            folderViewModel.folderId.value,
                                            note.id
                                        )
                                    }
                                    folderViewModel.getAllFolders()
                                    navController.popBackStack()
                                }
                            }
                        }
                    )
                }
            }

            Button(
                onClick = { showBottomSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Thêm thư mục",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Thư mục mới", fontSize = 16.sp)
                }
            }

            // Bottom Sheet
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    AddFolderBottomSheetContent(
                        onDismiss = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                showBottomSheet = false
                            }
                        },
                        onAddFolder = { folderName ->
                            scope.launch {
                                folderViewModel.addNewFolder(folderName)
                                sheetState.hide()
                            }.invokeOnCompletion {
                                showBottomSheet = false
                            }
                        }
                    )
                }
            }

            // Hiển thị dialog xác nhận xóa
            if (showDeleteDialog) {
                DeleteConfirmationDialog(
                    folderName = selectedFolder,
                    onDismissRequest = { showDeleteDialog = false },
                    onConfirmDelete = {
                        coroutineScope.launch {
                            val currentFolderId = folderViewModel.folderId.value
                            if (currentFolderId != null) {
                                noteModel.updateNotesInFolderNull(currentFolderId)
                                folderViewModel.deleteFolder(currentFolderId)
                            }
                        }
                    }
                )
            }
        }
    }
}





