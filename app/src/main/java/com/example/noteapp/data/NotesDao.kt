package com.example.noteapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Query("Select * from notes")
    fun getNotes(): Flow<List<NotesItem>>

    @Delete
    suspend fun deleteNotes(item: NotesItem)

    @Insert
    suspend fun addNotes(item: NotesItem):Long
}