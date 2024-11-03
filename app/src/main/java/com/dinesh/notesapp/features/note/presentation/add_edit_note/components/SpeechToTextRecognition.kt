package com.dinesh.notesapp.features.note.presentation.add_edit_note.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.dinesh.notesapp.R
import com.dinesh.notesapp.core.RecordAudioPermissionTextProvider
import com.dinesh.notesapp.core.checkPermissionStatus
import com.dinesh.notesapp.core.dialogue.PermissionDialog
import com.dinesh.notesapp.core.openAppSettings
import com.dinesh.notesapp.features.note.presentation.add_edit_note.AddEditNoteEvent
import com.dinesh.notesapp.features.note.presentation.add_edit_note.AddEditNoteState
import java.util.Locale

@Composable
fun SpeechToTextRecognition(
    addEditNoteState: AddEditNoteState,
    onEvent: (AddEditNoteEvent) -> Unit
) {

    val tag = "SpeechRecognition"

    val context = LocalContext.current as Activity

    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

    //Record audio permission
    val recordAudioPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onEvent(AddEditNoteEvent.PermissionGranted)
            } else if (
                !shouldShowRequestPermissionRationale(
                    context, Manifest.permission.RECORD_AUDIO
                )
            ) {
                onEvent(AddEditNoteEvent.PermissionRevoked)
            } else {
                onEvent(AddEditNoteEvent.ShowRationale)
            }
        }
    )

    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    LaunchedEffect(Unit) {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
                Log.d(tag, "onReadyForSpeech")
            }

            override fun onBeginningOfSpeech() {
                Log.d(tag, "onBeginningOfSpeech")
            }

            override fun onRmsChanged(p0: Float) {
                Log.d(tag, "onRmsChanged: $p0")
            }

            override fun onBufferReceived(p0: ByteArray?) {
                Log.d(tag, "onBufferReceived: $p0")
            }

            override fun onEndOfSpeech() {
                Log.d(tag, "onEndOfSpeech")
            }

            override fun onError(p0: Int) {
                Log.d(tag, "onError: $p0")
            }

            override fun onResults(p0: Bundle?) {
                p0?.let {
                    val data = it.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!data.isNullOrEmpty()) {
                        onEvent(AddEditNoteEvent.SpeechToTextContent(data[0]))
                    }
                }
                Log.d(tag, "onResults: ${p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)}")
            }

            override fun onPartialResults(p0: Bundle?) {
                Log.d(tag, "onPartialResults: ${p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)}")
            }

            override fun onEvent(p0: Int, p1: Bundle?) {
                Log.d(tag, "onEvent")
            }

        })
    }

    CircularImageView(
        icon = R.drawable.mike,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    if (context.checkPermissionStatus(Manifest.permission.RECORD_AUDIO)) {
                        speechRecognizer.startListening(intent)
                        awaitRelease()
                        speechRecognizer.stopListening()
                    } else {
                        recordAudioPermissionResultLauncher.launch(
                            Manifest.permission.RECORD_AUDIO
                        )
                    }
                }
            )
        }
    )

    if (addEditNoteState.showPermissionDialogue) {
        PermissionDialog(
            permissionTextProvider = RecordAudioPermissionTextProvider(),
            isPermanentlyDeclined = addEditNoteState.hasPermanentlyDeclined,
            onDismiss = {
                onEvent(AddEditNoteEvent.DismissDialogue)
            },
            onOkClick = {
                recordAudioPermissionResultLauncher.launch(
                    Manifest.permission.RECORD_AUDIO
                )
            },
            onAppSettingsClick = { context.openAppSettings() }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer.destroy()
        }
    }
}
