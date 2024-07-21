package com.dinesh.notesapp.features.note.domain.use_case

import com.dinesh.notesapp.features.note.domain.model.InvalidNoteException
import com.dinesh.notesapp.features.note.domain.model.Note
import com.dinesh.notesapp.features.note.domain.repository.NoteRepository
import javax.inject.Inject

class AddNote @Inject constructor(
    private val noteRepository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title of the note can't be empty")
        }

        if (note.content.isBlank()) {
            throw InvalidNoteException("The content of the note can't be empty")
        }

        noteRepository.insertNote(note)
    }
}