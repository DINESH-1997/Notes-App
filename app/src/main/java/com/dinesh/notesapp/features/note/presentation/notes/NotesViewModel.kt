package com.dinesh.notesapp.features.note.presentation.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dinesh.notesapp.features.note.domain.model.Note
import com.dinesh.notesapp.features.note.domain.use_case.AddNote
import com.dinesh.notesapp.features.note.domain.use_case.DeleteNote
import com.dinesh.notesapp.features.note.domain.use_case.GetNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotes,
    private val deleteNoteUseCase: DeleteNote,
    private val addNoteUseCase: AddNote
): ViewModel() {
    var notesState by mutableStateOf(NotesState())
        private set

    private var recentlyDeletedNote: Note? = null

    init {
        getAllNotes()
    }

    fun onEvent(event: NotesEvent) {
        when(event) {
            is NotesEvent.Delete -> {
                viewModelScope.launch {
                    deleteNoteUseCase(event.note)
                }
                recentlyDeletedNote = event.note
            }

            NotesEvent.RestoreNote -> {
                if (recentlyDeletedNote != null) {
                    viewModelScope.launch {
                        addNoteUseCase(recentlyDeletedNote!!)
                    }
                    recentlyDeletedNote = null
                }
            }
        }
    }

    private fun getAllNotes() {
        getNotesUseCase().onEach { notes ->
            notesState = notesState.copy(notes = notes)
        }.launchIn(viewModelScope)
    }
}