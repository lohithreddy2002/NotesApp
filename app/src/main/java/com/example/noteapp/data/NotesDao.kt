package com.example.noteapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Query("Select * from notes")
    fun getNotes(): Flow<List<NotesItem>>

    @Delete
    suspend fun deleteNotes(item: NotesItem)

    @Insert
    suspend fun addNotes(item: NotesItem): Long

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getSingleNote(id: Int): NotesItem

    @Update
    fun updateNote(note: NotesItem)

}