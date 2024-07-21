package com.dinesh.notesapp.features.note.presentation.notes

import com.dinesh.notesapp.features.note.domain.model.Note

sealed class NotesEvent {
    data class Delete(val note: Note): NotesEvent()
    data object RestoreNote: NotesEvent()
}