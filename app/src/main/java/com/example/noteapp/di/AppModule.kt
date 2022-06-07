package com.example.noteapp.di

import android.content.Context
import androidx.room.Room
import com.example.noteapp.data.NotesDao
import com.example.noteapp.data.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): NotesDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            NotesDatabase::class.java,
            "note_db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun getDao(database: NotesDatabase): NotesDao {
        return database.getNotesDao()
    }


}