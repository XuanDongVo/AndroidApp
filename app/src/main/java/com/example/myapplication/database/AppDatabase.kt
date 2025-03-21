package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.model.Folder
import com.example.myapplication.model.Note
import com.example.myapplication.model.Reminder
import com.example.myapplication.repository.FolderRepository
import com.example.myapplication.repository.NoteRepository
import com.example.myapplication.repository.ReminderRepository

@Database(entities = [Note::class, Reminder::class, Folder::class], version = 1   )
abstract class AppDatabase : RoomDatabase() {
    abstract fun folderDao(): FolderRepository
    abstract fun noteDao(): NoteRepository
    abstract fun reminderDao(): ReminderRepository

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            context.deleteDatabase("note_database")
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "note_database"
                )
//                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}