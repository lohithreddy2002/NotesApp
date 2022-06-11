package com.example.noteapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noteapp.ui.theme.Green
import com.example.noteapp.ui.theme.Magenta
import com.example.noteapp.ui.theme.Red
import com.example.noteapp.ui.theme.Yellow

@Entity(tableName = "notes")
data class NotesItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val noteDesc: String,
    val noteTitle: String,
    val noteColorCode: Int,
) {
    companion object {
        val colorList = listOf(
            Yellow,
            Red,
            Magenta,
            Green
        )
    }
}