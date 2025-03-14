package com.example.myapplication.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.model.Note
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = AppDatabase.getDatabase(application).noteDao()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addNote(title: String, content: String, colorBackground: Int) {
        val dateString =  LocalDateTime.now().format( DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        val note = Note(title = title , content = content,date = dateString, colorBackGround = colorBackground)
        noteDao.insertNote(note);
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }

    suspend  fun getAllNotes(): List<Note> {

        return noteDao.getAllNote()
    }
}
