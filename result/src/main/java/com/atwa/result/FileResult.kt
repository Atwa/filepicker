package com.atwa.result

import java.io.File

data class FileResult(
    val name: String?,
    val sizeKb: Int?,
    val file: File?
)