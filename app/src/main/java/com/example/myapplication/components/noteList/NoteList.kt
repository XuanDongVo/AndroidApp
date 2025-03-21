package com.example.myapplication.components.noteList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.model.Note
import com.example.myapplication.viewModel.NoteViewModel
import kotlinx.coroutines.launch


@Composable
fun NoteList(
    notes: List<Note>,
    noteModel : NoteViewModel,
    navController: NavController,
    onNewNoteClick: () -> Unit // Thêm tham số cho FAB
) {
    val notesState = remember { mutableStateOf<List<Note>>(notes) }
    var notes by notesState
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewNoteClick,
                modifier = Modifier.padding(18.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Thêm ghi chú")
            }
        }
    ) { innerPadding ->
        MaterialTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(notes) { note ->
                        SwipeableNoteItem(
                            note = note,
                            onDelete = { deletedNote ->
                                scope.launch {
                                    noteModel.deleteNote(deletedNote)
                                    notes = noteModel.getAllNotes()
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Note deleted",
                                        actionLabel = "Undo",
                                        duration = androidx.compose.material3.SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        noteModel.undoNote(note)
                                        notes = noteModel.getAllNotes()
                                    }
                                }
                            },
                            onClick = {
                                noteModel.selectNote(note)
                                navController.navigate("note_detail")
                            }
                        )
                    }
                }
            }
        }
    }
}