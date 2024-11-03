package com.dinesh.notesapp.features.note.presentation.add_edit_note.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dinesh.notesapp.R
import com.dinesh.notesapp.ui.theme.NotesAppTheme

@Composable
fun CircularImageView(
    icon: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = null,
        colorFilter = ColorFilter.tint(colorScheme.onPrimary),
        modifier = modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(colorScheme.primary)
            .padding(8.dp)
    )
}

@Preview
@Composable
fun CircularImageViewPreview() {
    NotesAppTheme {
        CircularImageView(R.drawable.mike)
    }
}