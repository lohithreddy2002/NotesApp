package com.example.noteapp

import com.example.noteapp.data.NotesDao
import com.example.noteapp.data.NotesItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class EditorRepository @Inject constructor(val dao: NotesDao) {

    suspend fun addNote(item: NotesItem) {
        withContext(Dispatchers.IO) {
            dao.addNotes(item)
        }
    }

}