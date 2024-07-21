package com.dinesh.notesapp.features.note.presentation.add_edit_note.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.dinesh.notesapp.ui.theme.NotesAppTheme

@Composable
fun CustomBasicTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    isHintVisible: Boolean = true,
    textStyle: TextStyle = TextStyle(),
    onValueChanged: (String)-> Unit,
    singleLine: Boolean = false
) {
    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            textStyle = textStyle.copy(
                color = colorScheme.onSurface
            ),
            cursorBrush = SolidColor(colorScheme.primary),
            onValueChange = onValueChanged,
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth(),
        )
        if (isHintVisible) {
            Text(text = hint, style = textStyle, color = colorScheme.onSurfaceVariant)
        }
    }
}

@Preview(showBackground = true)
@PreviewLightDark
@Composable
fun CustomBasicTextFieldPreview() {
    NotesAppTheme {
        var title by remember { mutableStateOf("") }
        CustomBasicTextField(
            text = title,
            hint = "Enter Title",
            isHintVisible = title.isBlank(),
            onValueChanged = { title = it },
            textStyle = typography.titleLarge,
            singleLine = true
        )
    }
}