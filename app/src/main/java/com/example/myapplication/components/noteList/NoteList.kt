package com.example.myapplication.components.noteList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Note
import com.example.myapplication.viewmodel.NoteViewModel


@Composable

fun NoteList(noteModel: NoteViewModel) {
    MaterialTheme { // Bao bọc toàn bộ giao diện trong MaterialTheme
        var notes by remember { mutableStateOf<List<Note>>(emptyList()) }
        // Sử dụng LaunchedEffect để thực thi code bất đồng bộ khi composable được gọi
        LaunchedEffect(Unit) {
            // Gọi suspend function từ viewModel
            notes = noteModel.getAllNotes()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Danh sách ghi chú
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp), // Thêm khoảng cách giữa các item
            ) {
                items(notes) { note ->
                    NoteItem(note = note, onDelete = {  })
                }
            }
        }
    }
}