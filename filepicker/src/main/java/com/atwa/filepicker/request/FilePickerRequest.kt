package com.atwa.filepicker.request

import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.atwa.filepicker.decoder.Decoder
import com.atwa.filepicker.result.FileMeta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.io.File

internal class FilePickerRequest(
    private val decoder: Decoder,
    private val onFilePicked: (FileMeta?) -> Unit,
    private val initialDirectoryPath: String?=null
) : PickerRequest {

    private val fileType = "*/*"

    override val intent: Intent
        get() = Intent(Intent.ACTION_GET_CONTENT).apply {
            if (initialDirectoryPath.isNullOrBlank())
                type = fileType
            else setDataAndType(initialDirectoryPath.toUri(), fileType)
        }

    override suspend fun invokeCallback(uri: Uri) {
        var result: FileMeta? = null
        decoder.getStorageFile(uri).collect { result = it }
        withContext(Dispatchers.Main) {
            onFilePicked(result)
        }
    }
}