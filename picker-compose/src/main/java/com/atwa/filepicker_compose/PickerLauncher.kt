package com.atwa.filepicker_compose

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult

data class PickerLauncher(
    val intent: Intent,
    val launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
){
    fun launch() {
        launcher.launch(intent)
    }
}

fun ManagedActivityResultLauncher<Intent, ActivityResult>.toPickerLauncher(intent: Intent) =
    PickerLauncher(
        intent = intent,
        launcher = this
    )

