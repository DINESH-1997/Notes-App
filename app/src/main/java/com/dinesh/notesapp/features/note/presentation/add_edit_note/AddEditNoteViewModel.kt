package com.dinesh.notesapp.features.note.presentation.add_edit_note

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinesh.notesapp.features.note.domain.use_case.AddNote
import com.dinesh.notesapp.features.note.domain.model.InvalidNoteException
import com.dinesh.notesapp.features.note.domain.model.Note
import com.dinesh.notesapp.features.note.domain.use_case.GetNote
import com.dinesh.notesapp.features.note.presentation.AddEditNotesScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNote,
    private val getNoteUseCase: GetNote,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var addEditNoteState by mutableStateOf(AddEditNoteState())
        private set

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("id")?.let {
            if (it != -1) getNote(it)
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when(event) {
            is AddEditNoteEvent.ContentChanged -> {
                addEditNoteState = addEditNoteState.copy(
                    noteContent = addEditNoteState.noteContent.copy(
                        text = event.value,
                        isHintVisible = event.value.isBlank()
                    )
                )
            }
            is AddEditNoteEvent.TitleChanged -> {
                addEditNoteState = addEditNoteState.copy(
                    noteTitle = addEditNoteState.noteTitle.copy(
                        text = event.value,
                        isHintVisible = event.value.isBlank()
                    )
                )
            }
            AddEditNoteEvent.SaveNote -> {
                saveNote()
            }
            is AddEditNoteEvent.GetNote -> {
                getNote(event.id)
            }

            //Permission handling events
            is AddEditNoteEvent.DismissDialogue -> {
                addEditNoteState = addEditNoteState.copy(
                    showPermissionDialogue = false
                )
            }
            is AddEditNoteEvent.PermissionGranted -> {
                addEditNoteState = addEditNoteState.copy(
                    hasRecordAudioPermission = true,
                    hasPermanentlyDeclined = false,
                    showPermissionDialogue = false
                )
            }
            is AddEditNoteEvent.PermissionRevoked -> {
                addEditNoteState = addEditNoteState.copy(
                    hasRecordAudioPermission = false,
                    hasPermanentlyDeclined = true,
                    showPermissionDialogue = true
                )
            }
            is AddEditNoteEvent.ShowRationale -> {
                addEditNoteState = addEditNoteState.copy(
                    hasRecordAudioPermission = false,
                    hasPermanentlyDeclined = false,
                    showPermissionDialogue = true
                )
            }

            is AddEditNoteEvent.SpeechToTextContent -> {
                addEditNoteState = addEditNoteState.copy(
                    noteContent = addEditNoteState.noteContent.copy(
                        text = "${addEditNoteState.noteContent.text} ${event.value}",
                        isHintVisible = event.value.isBlank()
                    )
                )
            }
        }
    }

    private fun getNote(id: Int) {
        viewModelScope.launch {
            val note = getNoteUseCase(id)
            note?.let {
                currentNoteId = id
                addEditNoteState = addEditNoteState.copy(
                    noteTitle = addEditNoteState.noteTitle.copy(
                        text = it.title, isHintVisible = false),
                    noteContent = addEditNoteState.noteContent.copy(
                        text = it.content, isHintVisible = false),
                )
            }
        }
    }

    private fun saveNote() {
        viewModelScope.launch {
            try {
                addNoteUseCase(
                    Note(
                        id = currentNoteId,
                        title = addEditNoteState.noteTitle.text,
                        content = addEditNoteState.noteContent.text,
                        timestamp = System.currentTimeMillis()
                    )
                )
                addEditNoteState = addEditNoteState.copy(
                    isNoteSavedSuccessful = true
                )
            } catch (e: InvalidNoteException) {
                addEditNoteState = addEditNoteState.copy(
                    isNoteSavedSuccessful = false,
                    errorMessage = e.message ?: "Couldn't save note"
                )
            }
        }
    }
}