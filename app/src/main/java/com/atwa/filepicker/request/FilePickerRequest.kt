package com.atwa.filepicker.request

import android.content.Intent
import android.net.Uri
import com.atwa.filepicker.decoder.Decoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.io.File

class FilePickerRequest(
    private val decoder: Decoder,
    private val onFilePicked: (Pair<String?, File?>?) -> Unit
) : PickerRequest {

    override val intent: Intent
        get() = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }

    override suspend fun invokeCallback(uri: Uri) {
        var result: Pair<String?, File?>? = null
        decoder.getStorageFile(uri).collect { result = it }
        withContext(Dispatchers.Main) {
            onFilePicked(result)
        }
    }
}