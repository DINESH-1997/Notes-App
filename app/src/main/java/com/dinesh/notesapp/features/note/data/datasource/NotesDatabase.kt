package com.dinesh.notesapp.features.note.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dinesh.notesapp.features.note.data.datasource.dao.NoteDao
import com.dinesh.notesapp.features.note.domain.model.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NotesDatabase: RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}