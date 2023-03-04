package com.atwa.filepicker.request

import android.content.Intent
import android.net.Uri
import com.atwa.filepicker.decoder.Decoder
import com.atwa.filepicker.result.FileMeta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.io.File

internal class PdfPickerRequest(
    private val decoder: Decoder,
    private val onPdfPicked: (FileMeta?) -> Unit
) : PickerRequest {
    override val intent: Intent
        get() = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }

    override suspend fun invokeCallback(uri: Uri) {
        var result: FileMeta? = null
        decoder.getStoragePDF(uri).collect { result = it }
        withContext(Dispatchers.Main) {
            onPdfPicked(result)
        }
    }
}