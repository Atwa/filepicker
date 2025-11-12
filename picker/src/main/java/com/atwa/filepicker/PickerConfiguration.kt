package com.atwa.filepicker

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Lifecycle
import com.atwa.core.decoder.Decoder

interface PickerConfiguration {
    val lifecycle: Lifecycle?
    val decoder: Decoder
    fun Intent.onPick(callback: (uri: Uri?) -> Unit)
    fun Intent.onMultiPick(callback: (uri: List<Uri?>) -> Unit)
    fun clear()
}