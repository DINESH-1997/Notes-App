package com.dinesh.notesapp.features.note.domain.use_case

import com.dinesh.notesapp.features.note.domain.model.Note
import com.dinesh.notesapp.features.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotes @Inject constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return noteRepository.getNotes()
    }
}