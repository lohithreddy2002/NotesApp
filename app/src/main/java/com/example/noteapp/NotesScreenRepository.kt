package com.example.noteapp

import com.example.noteapp.data.NotesDao
import com.example.noteapp.data.NotesItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesScreenRepository @Inject constructor(val dao: NotesDao) {
    fun getAllNotes(): Flow<List<NotesItem>> {

        return dao.getNotes()

    }
}