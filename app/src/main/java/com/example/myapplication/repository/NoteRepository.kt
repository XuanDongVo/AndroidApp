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

    @Query("UPDATE Note SET title = :title, content = :content, color= :color WHERE id = :noteId")
    suspend fun modifyNote(noteId: Int, title: String, content: String , color: Int)

    @Query("UPDATE Note SET isRemider = 1 WHERE id = :noteId")
    suspend fun setReminderNote(noteId:Int)

}