package com.atwa.filepicker.decoder

import android.net.Uri
import com.atwa.filepicker.result.FileMeta
import com.atwa.filepicker.result.ImageMeta
import com.atwa.filepicker.result.VideoMeta

interface Decoder {
    fun generateURI(isImageFile: Boolean): Uri?
    fun getStorageImage(imageUri: Uri?, onImagePicked: (ImageMeta?) -> Unit)
    fun getStorageImages(imageUris: List<Uri>, onImagesPicked: (List<ImageMeta?>) -> Unit)
    fun getStoragePDF(pdfUri: Uri?, onPdfPicked: (FileMeta?) -> Unit)
    fun getStorageFile(fileUri: Uri?, onFilePicked: (FileMeta?) -> Unit)
    fun getStorageVideo(videoUri: Uri?, onVideoPicked: (VideoMeta?) -> Unit)
    fun getCameraImage(imageUri: Uri?, onImagePicked: (ImageMeta?) -> Unit)
    fun getCameraVideo(videoUri: Uri?, onVideoPicked: (VideoMeta?) -> Unit)
    fun getStorageFiles(fileUris: List<Uri>, onFilesPicked: (List<FileMeta?>) -> Unit)
}