package com.example.myapplication.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "content") val content: String?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "color") val colorBackGround: Int = -1,
    val folder: Int? = null,
    @ColumnInfo(name = "isRemider") val isRemider: Boolean = false

)