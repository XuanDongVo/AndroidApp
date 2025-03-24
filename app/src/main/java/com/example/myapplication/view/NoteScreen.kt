package com.example.myapplication.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.components.noteList.NoteList
import com.example.myapplication.viewModel.FolderViewModel
import com.example.myapplication.viewModel.NoteViewModel
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    navController: NavController,
    noteViewModel: NoteViewModel,
    folderViewModel: FolderViewModel
) {
    var query by remember { mutableStateOf("") }
    val selectedTabState by folderViewModel.selectedFolder.collectAsState()
    val folderId by folderViewModel.folderId.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val notes by noteViewModel.notes.collectAsState()

    LaunchedEffect(selectedTabState) {
        Log.d("NoteScreen", "LaunchedEffect triggered: selectedTabState=$selectedTabState, folderId=$folderId")
        noteViewModel.getNotes(
            when (selectedTabState) {
                "Tất cả" -> -1
                "Chưa phân loại" -> null
                else -> folderId
            }
        )
        Log.d("NoteScreen", "Notes after fetch: ${noteViewModel.notes.value}")
    }


    val tabs by folderViewModel.folderList.collectAsState()

    val tabList = remember(tabs) {
        tabs.keys.toList()
    }


    Column(modifier = Modifier.fillMaxSize()) {
        // Thanh tìm kiếm
        SearchBar(query, onQueryChange = { query = it })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LazyRow chứa các tab, chỉ phần này cuộn được
            LazyRow(
                modifier = Modifier
                    .weight(1f, false)
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tabList) { tab ->
                    TabItem(
                        text = tab,
                        isSelected = selectedTabState == tab,
                        onClick = { coroutineScope.launch { folderViewModel.selectFolder(tab) } }
                    )
                }
            }

            // Icon thư mục (luôn đứng yên)
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = "Chọn thư mục",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.navigate("folder")}
            )
        }


        // Danh sách ghi chú
        NoteList(
            notes = notes,
            noteModel = noteViewModel,
            folderViewModel = folderViewModel,
            navController = navController,
            onNewNoteClick = { navController.navigate("newNoteScreen") } // Truyền hành động FAB
        )
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .then(
                if (isSelected) Modifier else Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                )
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() }
    )
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        placeholder = { Text(text = "Tìm kiếm...") },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}


