package com.dinesh.notesapp.features.note.presentation.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dinesh.notesapp.features.note.domain.model.Note
import com.dinesh.notesapp.features.note.presentation.AddEditNotesScreen
import com.dinesh.notesapp.features.note.presentation.notes.components.NoteCard
import com.dinesh.notesapp.features.note.presentation.notes.components.NoteEventCallBack
import com.dinesh.notesapp.ui.theme.NotesAppTheme
import kotlinx.coroutines.launch

@Composable
fun NotesListScreen(
    notesState: NotesState,
    onEvent: (NotesEvent) -> Unit,
    navController: NavController,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { Snackbar(snackbarData = it) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AddEditNotesScreen())
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
            }
        }
    ) { padding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(200.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(notesState.notes) { note ->
                    NoteCard(
                        note = note,
                        noteEventCallBack = object : NoteEventCallBack {
                            override fun onEdit(id: Int?) {
                                navController.navigate(AddEditNotesScreen(id!!))
                            }

                            override fun onDelete(note: Note) {
                                onEvent(NotesEvent.Delete(note))
                                scope.launch {
                                    val result = snackBarHostState.showSnackbar(
                                        message = "Note deleted successfully",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Long
                                    )
                                    when (result) {
                                        SnackbarResult.Dismissed -> {}
                                        SnackbarResult.ActionPerformed -> {
                                            onEvent(NotesEvent.RestoreNote)
                                        }
                                    }
                                }
                            }

                        }
                    )
                }
            },
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NotesListScreenPreview() {
    NotesAppTheme {
        NotesListScreen (
            navController = rememberNavController(),
            onEvent = {},
            notesState = NotesState()
        )
    }
}