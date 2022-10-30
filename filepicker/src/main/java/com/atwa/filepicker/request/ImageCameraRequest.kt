package com.atwa.filepicker.request

import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import com.atwa.filepicker.decoder.Decoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.io.File

internal class ImageCameraRequest(
    private val decoder: Decoder,
    private val onPhotoTaken: (Pair<Bitmap?, File?>?) -> Unit
) {

    val intent: Intent
        get() = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    suspend fun invokeCameraCallback(bitmap: Bitmap) {
        var result: Pair<Bitmap?, File?>? = null
        decoder.saveStorageImage(bitmap).collect { result = it }
        withContext(Dispatchers.Main) {
            onPhotoTaken(result)
        }
    }
}