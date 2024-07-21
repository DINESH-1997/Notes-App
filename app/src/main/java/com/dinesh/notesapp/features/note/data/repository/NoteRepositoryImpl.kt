package com.dinesh.notesapp.features.note.data.repository

import com.dinesh.notesapp.features.note.domain.model.Note
import com.dinesh.notesapp.features.note.data.datasource.dao.NoteDao
import com.dinesh.notesapp.features.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
): NoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNote(id: Int): Note? {
        return dao.getNote(id)
    }

    override suspend fun insertNote(note: Note) {
        return dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        return dao.deleteNote(note)
    }
}