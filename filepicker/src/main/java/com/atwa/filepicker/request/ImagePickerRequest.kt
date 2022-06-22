package com.atwa.filepicker.request

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.atwa.filepicker.decoder.Decoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.io.File

internal class ImagePickerRequest(
    private val decoder: Decoder,
    private val onImagePicked: (Pair<Bitmap?, File?>?) -> Unit
) : PickerRequest {
    override val intent: Intent
        get() = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

    override suspend fun invokeCallback(uri: Uri) {
        var result: Pair<Bitmap?, File?>? = null
        decoder.getStorageImage(uri).collect { result = it }
        withContext(Dispatchers.Main) {
            onImagePicked(result)
        }
    }

}