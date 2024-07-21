package com.dinesh.notesapp.di

import android.app.Application
import androidx.room.Room
import com.dinesh.notesapp.features.note.data.datasource.NotesDatabase
import com.dinesh.notesapp.features.note.data.repository.NoteRepositoryImpl
import com.dinesh.notesapp.features.note.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteAppCoreModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(
        app: Application
    ): NotesDatabase {
        return Room.databaseBuilder(
            app,
            NotesDatabase::class.java,
            NotesDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideNoteRepository(
        noteDb: NotesDatabase
    ): NoteRepository {
        return NoteRepositoryImpl(noteDb.noteDao)
    }
}