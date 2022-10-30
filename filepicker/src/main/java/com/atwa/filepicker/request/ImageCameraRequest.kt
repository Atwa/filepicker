package com.atwa.filepicker.request

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.atwa.filepicker.decoder.Decoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.TimeUnit

internal class ImageCameraRequest(
    private val decoder: Decoder,
    private val onPhotoTaken: (Pair<Bitmap?, File?>?) -> Unit
) : PickerRequest {

    private val outputUri : Uri by lazy { decoder.createCameraOutputUri() }

    override val intent: Intent
        get() = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        }


    override suspend fun invokeCallback(uri: Uri) {
        var result: Pair<Bitmap?, File?>? = null
        decoder.getStorageImage(outputUri).collect { result = it }
        withContext(Dispatchers.Main) {
            onPhotoTaken(result)
        }
    }
}