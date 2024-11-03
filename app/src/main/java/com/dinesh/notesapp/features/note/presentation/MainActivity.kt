package com.dinesh.notesapp.features.note.presentation

import AddEditNoteScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dinesh.notesapp.features.note.presentation.add_edit_note.AddEditNoteEvent
import com.dinesh.notesapp.features.note.presentation.add_edit_note.AddEditNoteViewModel
import com.dinesh.notesapp.features.note.presentation.notes.NotesListScreen
import com.dinesh.notesapp.features.note.presentation.notes.NotesViewModel
import com.dinesh.notesapp.ui.theme.NotesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NotesListScreen
                ) {
                    composable<NotesListScreen> {
                        val notesViewModel = hiltViewModel<NotesViewModel>()
                        val notesState = notesViewModel.notesState
                        NotesListScreen(
                            notesState = notesState,
                            onEvent = notesViewModel::onEvent,
                            navController = navController
                        )
                    }
                    composable<AddEditNotesScreen> {
                        val addEditNoteViewModel = hiltViewModel<AddEditNoteViewModel>()
                        val addEditNoteState = addEditNoteViewModel.addEditNoteState
                        AddEditNoteScreen(
                            addEditNoteState = addEditNoteState,
                            onEvent = addEditNoteViewModel::onEvent,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Serializable
object NotesListScreen

@Serializable
data class AddEditNotesScreen(val id: Int = -1)