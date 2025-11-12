package com.atwa.result

import android.graphics.Bitmap
import java.io.File

data class ImageResult(
    val name: String?,
    val sizeKb: Int?,
    val file: File?,
    val bitmap: Bitmap?
)