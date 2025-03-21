package com.example.myapplication.viewModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = AppDatabase.getDatabase(application).noteDao()

    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote.asStateFlow()


    fun selectNote(note: Note) {
        _selectedNote.value = note
    }

    // khôi phục note
    suspend fun undoNote(note: Note) {
        noteDao.insertNote(note);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addNote(title: String, content: String, colorBackground: Int, folder: Int?) {
        val dateString =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        val note = Note(
            title = title,
            content = content,
            date = dateString,
            folder = folder,
            colorBackGround = colorBackground
        )
        noteDao.insertNote(note);
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun getAllNotes(): List<Note> {
        return noteDao.getAllNote()
    }

    suspend fun modifyNote(noteId: Int, title: String, content: String, color: Int) {
        return noteDao.modifyNote(noteId, title, content, color)
    }

    suspend fun getNotes(folderId: Int?): List<Note> {
        return when (folderId) {
            null -> noteDao.getNotesWithFolderNull()
            -1 -> noteDao.getAllNote()
            else -> noteDao.getNotesInFolder(folderId)
        }
    }

    suspend fun hasNotesWithFolderNull(): Int {
        return noteDao.hasNotesWithFolderNull()
    }

}
