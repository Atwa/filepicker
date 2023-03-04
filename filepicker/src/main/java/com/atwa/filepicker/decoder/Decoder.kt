package com.atwa.filepicker.decoder

import android.graphics.Bitmap
import android.net.Uri
import com.atwa.filepicker.result.FileMeta
import com.atwa.filepicker.result.ImageMeta
import com.atwa.filepicker.result.VideoMeta
import kotlinx.coroutines.flow.Flow

internal interface Decoder {
    fun getStorageImage(imageUri: Uri?): Flow<ImageMeta?>
    fun getStoragePDF(pdfUri: Uri?): Flow<FileMeta?>
    fun getStorageFile(pdfUri: Uri?): Flow<FileMeta?>
    fun saveStorageImage(bitmap: Bitmap): Flow<ImageMeta?>
    fun getStorageVideo(videoUri: Uri?): Flow<VideoMeta?>
}