package com.example.myapplication.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.model.Reminder

@Dao
interface ReminderRepository {

    @Insert
    suspend fun insertReminder(reminder: Reminder)

    @Query("DELETE FROM  reminders WHERE noteId = :noteId")
    suspend fun deleteReminDer(noteId: Int);

    @Query("UPDATE reminders SET timeInMillis = :timeInMillis, date = :date WHERE noteId = :noteId")
    suspend fun modifyReminders(noteId: Int, timeInMillis: Long, date: String)
}