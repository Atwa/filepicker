package com.atwa.filepicker.decoder

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeFileDescriptor
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.renderscript.ScriptGroup.Input
import android.util.Size
import com.atwa.filepicker.result.FileMeta
import com.atwa.filepicker.result.ImageMeta
import com.atwa.filepicker.result.VideoMeta
import com.atwa.filepicker.stream.FileStreamer
import com.atwa.filepicker.stream.Streamer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.*
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit


internal class UriDecoder(
    private val context: Context?,
    private val streamer: Streamer = FileStreamer()
) : Decoder {

    private var uri: Uri? = null
    private var contentResolver: ContentResolver? = null

    override fun getStorageImage(imageUri: Uri?) = flow {
        uri = imageUri
        contentResolver = context?.contentResolver
        decodeImage().also { emit(it) }
    }

    override fun getStoragePDF(pdfUri: Uri?) = flow {
        uri = pdfUri
        contentResolver = context?.contentResolver
        decodeFile().also { emit(it) }
    }

    override fun getStorageFile(pdfUri: Uri?) = flow {
        uri = pdfUri
        contentResolver = context?.contentResolver
        decodeFile().also { emit(it) }
    }

    override fun saveStorageImage(bitmap: Bitmap): Flow<ImageMeta?> = flow {
        saveImageToFile(bitmap).also { emit((it)) }
    }

    override fun getStorageVideo(videoUri: Uri?): Flow<VideoMeta?> = flow {
        uri = videoUri
        contentResolver = context?.contentResolver
        decodeVideo().also { emit(it) }
    }

    private fun saveImageToFile(bitmap: Bitmap): ImageMeta? {
        var byteStream: ByteArrayInputStream? = null
        return try {
            val fileName = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()
            val imageFile = File(context?.cacheDir, fileName)
            val size = imageFile.length().getFileSize()

            val byteArray = (bitmap.allocationByteCount * bitmap.height).run {
                ByteBuffer.allocate(this)
            }.apply { bitmap.copyPixelsToBuffer(this) }.array()
            byteStream = ByteArrayInputStream(byteArray)
            val outputStream = FileOutputStream(imageFile)

            streamer.copyFile(byteStream, outputStream)
            ImageMeta(fileName, size, imageFile, bitmap)
        } catch (e: Exception) {
            println(e.message)
            null
        } finally {
            byteStream?.close()
        }
    }

    private fun decodeImage(): ImageMeta? {
        var pfd: ParcelFileDescriptor? = null
        return try {
            uri?.let { uri ->
                pfd = contentResolver?.openFileDescriptor(uri, "r")
                pfd?.let { fd ->
                    val inputStream = FileInputStream(fd.fileDescriptor)
                    val bitmap = decodeFileDescriptor(fd.fileDescriptor)
                    val meta = getFileMeta() ?: Pair("file", null)
                    val imageFile = File(context?.cacheDir, meta.first)
                    val outputStream = FileOutputStream(imageFile)
                    streamer.copyFile(inputStream, outputStream)
                    ImageMeta(meta.first, meta.second, imageFile, bitmap)
                }
            }
        } catch (e: Exception) {
            println(e.message)
            null
        } finally {
            pfd?.close()
        }
    }

    private fun decodeVideo(): VideoMeta? {
        var inputStream: InputStream? = null
        return try {
            uri?.let { uri ->
                inputStream = contentResolver?.openInputStream(uri)
                val fileName = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()
                val videoFile = File(context?.cacheDir, fileName)
                val outputStream = FileOutputStream(videoFile)
                inputStream?.let { streamer.copyFile(it, outputStream) }
                val preview = ThumbnailUtils.createVideoThumbnail(
                    videoFile.path,
                    MediaStore.Video.Thumbnails.MINI_KIND
                )
                VideoMeta(fileName, videoFile.length().getFileSize(), videoFile, preview)
            }
        } catch (e: Exception) {
            println(e.message)
            null
        } finally {
            inputStream?.close()
        }
    }

    private fun decodeFile(): FileMeta? {
        var pfd: ParcelFileDescriptor? = null
        return try {
            uri?.let { pdfUri ->
                pfd = contentResolver?.openFileDescriptor(pdfUri, "r")
                val inputStream = FileInputStream(pfd?.fileDescriptor)
                val meta = getFileMeta() ?: Pair("file", null)
                val pdfFile = File(context?.cacheDir, meta.first)
                val outputStream = FileOutputStream(pdfFile)
                streamer.copyFile(inputStream, outputStream)
                FileMeta(meta.first, meta.second, pdfFile)
            }
        } catch (e: Exception) {
            println(e.message)
            null
        } finally {
            pfd?.close()

        }
    }

    @SuppressLint("Range")
    private fun getFileMeta(): Pair<String, Int?>? {
        return uri?.let { uri ->
            var result: Pair<String, Int?>? = null
            if (uri.scheme == "content") {
                val cursor = contentResolver?.query(uri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    val size =
                        cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE)).getFileSize()
                    result = Pair(name ?: "file", size)
                    cursor.close()
                }
            }
            if (result == null) {
                val path = uri.path
                val cut = path?.lastIndexOf('/')
                if (cut != -1) {
                    val name = path?.substring(cut?.plus(1) ?: 0)
                    result = Pair(name ?: "file", null)
                }
            }
            result
        }
    }

}

private fun Long?.getFileSize(): Int? {
    return this?.div(1000)?.toInt()
}


