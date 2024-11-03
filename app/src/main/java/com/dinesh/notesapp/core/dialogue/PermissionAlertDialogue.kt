package com.dinesh.notesapp.core.dialogue

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dinesh.notesapp.R
import com.dinesh.notesapp.core.PermissionTextProvider

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (isPermanentlyDeclined) {
                        onAppSettingsClick()
                    } else {
                        onOkClick()
                    }
                }
            ) {
                Text(
                    if (isPermanentlyDeclined) {
                        stringResource(R.string.app_settings)
                    } else {
                        stringResource(R.string.ok)
                    }
                )
            }
        },
        title = {
            Text(
                permissionTextProvider.getTitle()
            )
        },
        text = {
            Text(
                permissionTextProvider.getDescription(isPermanentlyDeclined)
            )
        },
    )
}