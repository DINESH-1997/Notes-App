package com.dinesh.notesapp.features.note.presentation.notes

import com.dinesh.notesapp.features.note.domain.model.Note

data class NotesState(
    val notes: List<Note> = emptyList()
)
