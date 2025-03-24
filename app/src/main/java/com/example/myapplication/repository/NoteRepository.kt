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
    suspend fun getAllNote(): List<Note>

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("UPDATE Note SET title = :title, content = :content, color= :color WHERE id = :noteId")
    suspend fun modifyNote(noteId: Int, title: String, content: String, color: Int)

    @Query("UPDATE Note SET isRemider = 1 WHERE id = :noteId")
    suspend fun setReminderNote(noteId: Int)

    @Query("SELECT * FROM Note WHERE folder is null")
    suspend fun getNotesWithFolderNull() : List<Note>;

    @Query("SELECT * FROM Note WHERE folder = :id")
    suspend fun getNotesInFolder(id: Int) : List<Note>;

    @Query("SELECT COUNT(*) FROM Note WHERE folder IS NULL")
    suspend fun hasNotesWithFolderNull(): Int

    @Query("SELECT COUNT(*) FROM Note WHERE folder =:id")
    suspend fun countNotesInFolder(id:Int):Int

    @Query("UPDATE Note set folder = :folder WHERE id =:id")
    suspend fun updateNoteInFolderNull(folder: Int, id: Int)

    @Query("UPDATE Note set folder = NULL WHERE id =:id")
    suspend fun updateNoteInFolderNull( id: Int)

    @Query("UPDATE NOTE set folder = NULL WHERE folder= :folderId")
    suspend fun updateNotesInFolderNull(folderId: Int)

}