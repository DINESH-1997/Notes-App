package com.dinesh.notesapp.features.note.domain.use_case

import com.dinesh.notesapp.features.note.domain.model.Note
import com.dinesh.notesapp.features.note.domain.repository.NoteRepository
import javax.inject.Inject

class GetNote @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return noteRepository.getNote(id)
    }
}