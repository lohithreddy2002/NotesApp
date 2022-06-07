package com.example.noteapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NotesItem::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun getNotesDao(): NotesDao

}