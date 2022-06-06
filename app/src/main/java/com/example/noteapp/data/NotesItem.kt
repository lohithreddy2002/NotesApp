package com.example.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NotesItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val noteDesc:String,
    val noteTitle:String,
    val NoteColorCode:Int,
)