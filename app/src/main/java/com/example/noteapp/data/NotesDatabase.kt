package com.example.noteapp.data

import androidx.room.Database

@Database(entities = [NotesItem::class], version = 1)
abstract class NotesDatabase() {

}