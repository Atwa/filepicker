package com.atwa.filepicker.request

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.atwa.filepicker.decoder.Decoder
import com.atwa.filepicker.result.VideoMeta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

internal class VideoPickerRequest(
    private val decoder: Decoder,
    private val onVideoPicked: (VideoMeta?) -> Unit
) : PickerRequest {

    override val intent: Intent
        get() = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )

    override suspend fun invokeCallback(uri: Uri) {
        var result: VideoMeta? = null
        decoder.getStorageVideo(uri).collect { result = it }
        withContext(Dispatchers.Main) {
            onVideoPicked(result)
        }
    }
}