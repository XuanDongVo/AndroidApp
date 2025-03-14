package com.example.myapplication.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.model.Note

@Dao
interface NoteRepository {
    @Insert
    suspend fun insertNote(note: Note)

    @Query("Select * from Note")
    suspend  fun getAllNote(): List<Note>

    @Delete
    suspend fun deleteNote(note: Note)

}