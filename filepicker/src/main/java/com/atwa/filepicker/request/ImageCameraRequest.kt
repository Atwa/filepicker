package com.atwa.filepicker.request

import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import com.atwa.filepicker.decoder.Decoder
import com.atwa.filepicker.result.ImageMeta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.io.File

internal class ImageCameraRequest(
    private val decoder: Decoder,
    private val onPhotoTaken: (ImageMeta?) -> Unit
) {

    val intent: Intent
        get() = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    suspend fun invokeCameraCallback(bitmap: Bitmap) {
        var result: ImageMeta? = null
        decoder.saveStorageImage(bitmap).collect { result = it }
        withContext(Dispatchers.Main) {
            onPhotoTaken(result)
        }
    }
}