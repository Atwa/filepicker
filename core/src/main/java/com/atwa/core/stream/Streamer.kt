package com.atwa.core.stream

import java.io.InputStream
import java.io.OutputStream

interface Streamer {
    fun copyFile(
        inputStream: InputStream,
        outputStream: OutputStream,
        totalBytes: Long? = null,
        onProgress: ((Float) -> Unit)? = null
    )
}