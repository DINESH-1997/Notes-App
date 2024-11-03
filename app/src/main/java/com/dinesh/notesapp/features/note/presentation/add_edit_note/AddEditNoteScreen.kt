import android.Manifest
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dinesh.notesapp.core.checkPermissionStatus
import com.dinesh.notesapp.features.note.presentation.add_edit_note.AddEditNoteEvent
import com.dinesh.notesapp.features.note.presentation.add_edit_note.AddEditNoteState
import com.dinesh.notesapp.features.note.presentation.add_edit_note.components.CustomBasicTextField
import com.dinesh.notesapp.features.note.presentation.add_edit_note.components.SpeechToTextRecognition
import com.dinesh.notesapp.ui.theme.NotesAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    addEditNoteState: AddEditNoteState,
    onEvent: (AddEditNoteEvent) -> Unit,
    navController: NavController
) {
    val tag = "AddEditNoteScreen"

    val context = LocalContext.current as Activity

    LaunchedEffect(Unit) {
        when {
            context.checkPermissionStatus(Manifest.permission.RECORD_AUDIO) -> {
                onEvent(AddEditNoteEvent.PermissionGranted)
            }

            else -> {
                Log.d(tag, "Request permission from user.")
            }
        }
    }

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(addEditNoteState.errorMessage) {
        addEditNoteState.errorMessage?.let {
            snackBarHostState.showSnackbar(
                message = addEditNoteState.errorMessage
            )
        }
    }

    LaunchedEffect(addEditNoteState.isNoteSavedSuccessful) {
        if (addEditNoteState.isNoteSavedSuccessful) {
            navController.navigateUp()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { Snackbar(snackbarData = it) }
            )
        },
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(AddEditNoteEvent.SaveNote) },
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "save note"
                )
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(start = 14.dp, end = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomBasicTextField(
                text = addEditNoteState.noteTitle.text,
                hint = addEditNoteState.noteTitle.hint,
                isHintVisible = addEditNoteState.noteTitle.isHintVisible,
                onValueChanged = { onEvent(AddEditNoteEvent.TitleChanged(it)) },
                textStyle = typography.titleLarge,
                singleLine = true
            )
            Row {
                CustomBasicTextField(
                    text = addEditNoteState.noteContent.text,
                    hint = addEditNoteState.noteContent.hint,
                    isHintVisible = addEditNoteState.noteContent.isHintVisible,
                    onValueChanged = { onEvent(AddEditNoteEvent.ContentChanged(it)) },
                    textStyle = typography.bodyLarge,
                    singleLine = false,
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(end = 8.dp)
                )
                SpeechToTextRecognition(addEditNoteState, onEvent)
            }
        }
    }
}

@Preview
@PreviewLightDark
@Composable
fun AddEditNotesScreenPreview() {
    NotesAppTheme {
        AddEditNoteScreen(
            addEditNoteState = AddEditNoteState(),
            onEvent = {},
            navController = rememberNavController()
        )
    }
}
