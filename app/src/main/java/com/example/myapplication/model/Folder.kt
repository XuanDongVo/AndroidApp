package com.example.myapplication.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Folder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
     val name: String,
    val isSelect: Boolean
)