package com.atwa.filepicker.result

import android.graphics.Bitmap
import java.io.File

data class ImageMeta(
    val name: String?,
    val sizeKb: Int?,
    val file: File?,
    val bitmap: Bitmap?
)