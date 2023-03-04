package com.atwa.filepicker.result

import java.io.File

data class FileMeta(
    val name: String?,
    val sizeKb: Int?,
    val file: File?
)