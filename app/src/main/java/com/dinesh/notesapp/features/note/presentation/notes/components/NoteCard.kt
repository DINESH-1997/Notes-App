package com.dinesh.notesapp.features.note.presentation.notes.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.dinesh.notesapp.features.note.domain.model.Note
import com.dinesh.notesapp.ui.theme.NotesAppTheme

interface NoteEventCallBack {
    fun onEdit(id: Int?)
    fun onDelete(note: Note)
}

@Composable
fun NoteCard(
    note: Note,
    noteEventCallBack: NoteEventCallBack
) {
    val density = LocalDensity.current
    var dropDownMenuState by remember { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }
    Card(
        modifier = Modifier
            .padding(4.dp)
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
    ) {
        Box(
            modifier = Modifier.padding(12.dp)
                .pointerInput(true) {
                    detectTapGestures(
                        onLongPress = {
                            dropDownMenuState = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        }
                    )
                }
        ) {
            Column {
                Text(
                    note.title,
                    style = typography.titleLarge,
                    color = colorScheme.onSurface
                )
                Text(
                    note.content,
                    style = typography.bodySmall,
                    color = colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 5
                )
            }
        }
        DropdownMenu(
            expanded = dropDownMenuState,
            onDismissRequest = { dropDownMenuState = false },
            offset = pressOffset.copy(
                y = pressOffset.y - itemHeight
            )
        ) {
            DropdownMenuItem(
                text = { Text("Edit") },
                leadingIcon = { Icon(Icons.Default.Edit, "edit") },
                onClick = {
                    noteEventCallBack.onEdit(note.id)
                    dropDownMenuState = false
                }
            )
            DropdownMenuItem(
                text = { Text("Delete") },
                leadingIcon = { Icon(Icons.Default.Delete, "delete") },
                onClick = {
                    noteEventCallBack.onDelete(note)
                    dropDownMenuState = false
                }
            )
        }
    }
}

@Preview
@Composable
fun NoteCardPreview() {
    NotesAppTheme {
        NoteCard(
            note = Note(
                title = "Test Note Title!",
                content = "Test note content...",
                timestamp = 5608098457
            ),
            noteEventCallBack = object : NoteEventCallBack {
                override fun onEdit(id: Int?) {
                    TODO("Not yet implemented")
                }

                override fun onDelete(note: Note) {
                    TODO("Not yet implemented")
                }

            }
        )
    }
}
