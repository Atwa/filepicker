package com.atwa.filepicker.core

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import com.atwa.filepicker.decoder.Decoder

interface PickerConfiguration {
    val lifecycle: Lifecycle?
    val decoder: Decoder
    fun Intent.onPick(callback: (uri: Uri?) -> Unit)
    fun Intent.onMultiPick(callback: (uri: List<Uri?>) -> Unit)
    fun clear()
}