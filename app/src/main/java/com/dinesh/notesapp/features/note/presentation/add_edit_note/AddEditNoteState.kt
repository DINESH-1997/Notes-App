package com.dinesh.notesapp.features.note.presentation.add_edit_note

data class AddEditNoteState(
    val noteTitle: TextFieldState = TextFieldState(hint = "Title"),
    val noteContent: TextFieldState = TextFieldState(hint = "Note"),
    val errorMessage: String? = null,
    val isNoteSavedSuccessful: Boolean = false
)