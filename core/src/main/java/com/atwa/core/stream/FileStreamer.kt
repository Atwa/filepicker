package com.atwa.core.stream

import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.InputStream
import java.io.OutputStream

class FileStreamer : Streamer {

    companion object {
        private const val BASE_BUFFER_SIZE = 32 * 1024 // 256 KB
    }

    override fun copyFile(
        inputStream: InputStream,
        outputStream: OutputStream,
        totalBytes: Long?,
        onProgress: ((progress: Float) -> Unit)?
    ) {
        BufferedInputStream(inputStream).use { input ->
            BufferedOutputStream(outputStream).use { output ->
                val buffer = ByteArray(BASE_BUFFER_SIZE)
                var bytesCopied = 0L
                var bytesRead: Int
                var lastUpdate = 0L

                while (input.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                    bytesCopied += bytesRead

                    // Update progress every 1 MB or on completion
                    if (onProgress != null && totalBytes != null &&
                        bytesCopied - lastUpdate >= BASE_BUFFER_SIZE
                    ) {
                        val progress = bytesCopied.toFloat() / totalBytes
                        Log.d("FileStreamer", "Copy progress: $progress")
                        onProgress(progress.coerceAtMost(1f))
                        lastUpdate = bytesCopied
                    }
                }

                output.flush()
                onProgress?.invoke(1f) // complete
            }
        }
    }

}