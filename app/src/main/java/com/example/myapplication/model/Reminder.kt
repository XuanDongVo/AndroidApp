package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val noteId: Int,
    val timeInMillis: Long,
    val date: String
)