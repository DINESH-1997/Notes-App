package com.dinesh.notesapp.core

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
    fun getTitle(): String
}

class RecordAudioPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "Please grant permission in app settings for recording audio.\n\n" +
                    "To enable this, click App Settings below and activate this feature under the permission menu"
        } else {
            "Notes app needs access to your microphone to record audio"
        }
    }

    override fun getTitle(): String {
        return "Notes app needs to access contacts"
    }
}