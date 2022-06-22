package com.atwa.filepicker.decoder

import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.io.File

internal interface Decoder {
    fun getStorageImage(imageUri: Uri?): Flow<Pair<Bitmap?, File?>?>
    fun getStoragePDF(pdfUri: Uri?): Flow<Pair<String?, File>?>
    fun getStorageFile(pdfUri: Uri?): Flow<Pair<String?, File>?>
}