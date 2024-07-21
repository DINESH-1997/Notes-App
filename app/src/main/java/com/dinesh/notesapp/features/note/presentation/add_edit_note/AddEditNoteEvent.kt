package com.dinesh.notesapp.features.note.presentation.add_edit_note

sealed class AddEditNoteEvent {
    data class TitleChanged(val value: String): AddEditNoteEvent()
    data class ContentChanged(val value: String): AddEditNoteEvent()
    data class GetNote(val id: Int): AddEditNoteEvent()
    data object SaveNote: AddEditNoteEvent()
}