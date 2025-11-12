package com.atwa.core.decoder

import android.net.Uri
import com.atwa.result.FileResult
import com.atwa.result.ImageResult
import com.atwa.result.VideoResult

interface Decoder {
    fun generateURI(isImageFile: Boolean): Uri?
    fun getStorageImage(
        imageUri: Uri?,
        onProgress: ((Float) -> Unit)? = null,
        onImagePicked: (ImageResult?) -> Unit
    )

    fun getStorageImages(
        imageUris: List<Uri>,
        onProgress: ((Int, Int, Float) -> Unit)? = null,
        onImagesPicked: (List<ImageResult?>) -> Unit
    )

    fun getStoragePDF(
        pdfUri: Uri?,
        onProgress: ((Float) -> Unit)? = null,
        onPdfPicked: (FileResult?) -> Unit
    )

    fun getStorageFile(
        fileUri: Uri?,
        onProgress: ((Float) -> Unit)? = null,
        onFilePicked: (FileResult?) -> Unit
    )

    fun getStorageVideo(
        videoUri: Uri?,
        onProgress: ((Float) -> Unit)? = null,
        onVideoPicked: (VideoResult?) -> Unit
    )

    fun getCameraImage(imageUri: Uri?, onImagePicked: (ImageResult?) -> Unit)
    fun getCameraVideo(videoUri: Uri?, onVideoPicked: (VideoResult?) -> Unit)
    fun getStorageFiles(
        fileUris: List<Uri>,
        onProgress: ((Int, Int, Float) -> Unit)? = null,
        onFilesPicked: (List<FileResult?>) -> Unit
    )
}
