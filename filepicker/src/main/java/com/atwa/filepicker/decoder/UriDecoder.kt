package com.atwa.filepicker.decoder

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeFileDescriptor
import android.net.Uri
import android.provider.OpenableColumns
import com.atwa.filepicker.stream.FileStreamer
import com.atwa.filepicker.stream.Streamer
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

internal class UriDecoder(
    private val context: Context?,
    private val streamer: Streamer = FileStreamer()
) : Decoder {

    private var uri: Uri? = null
    private var contentResolver: ContentResolver? = null

    override fun getStorageImage(imageUri: Uri?) = flow {
        this@UriDecoder.uri = imageUri
        this@UriDecoder.contentResolver = context?.contentResolver
        val result = try {
            getBitMap()
        } catch (ex: IOException) {
            println(ex.message)
            null
        }
        emit(result)
    }

    override fun getStoragePDF(pdfUri: Uri?) = flow {
        this@UriDecoder.uri = pdfUri
        this@UriDecoder.contentResolver = context?.contentResolver
        val result = try {
            getFile()
        } catch (ex: IOException) {
            println(ex.message)
            null
        }
        emit(result)
    }

    override fun getStorageFile(pdfUri: Uri?) = flow {
        this@UriDecoder.uri = pdfUri
        this@UriDecoder.contentResolver = context?.contentResolver
        val result = try {
            getFile()
        } catch (ex: IOException) {
            println(ex.message)
            null
        }
        emit(result)
    }

    override fun createCameraOutputUri(): Uri {
        val timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()
        return Uri.parse("${context?.cacheDir}$timeStamp")
    }


    private fun getBitMap(): Pair<Bitmap?, File?>? {
        return uri?.let { uri ->
            contentResolver?.openFileDescriptor(uri, "r")?.let { pfd ->
                val inputStream = FileInputStream(pfd.fileDescriptor)
                val bitmap = decodeFileDescriptor(pfd.fileDescriptor)
                val name = getFileName() ?: "file"
                val imageFile = File(context?.cacheDir, name)
                val outputStream = FileOutputStream(imageFile)
                streamer.copyFile(inputStream, outputStream)
                Pair(bitmap, imageFile)
            }
        }
    }

    private fun getFile(): Pair<String?, File>? {
        return try {
            uri?.let { pdfUri ->
                val pfd = contentResolver?.openFileDescriptor(pdfUri, "r")
                val inputStream = FileInputStream(pfd?.fileDescriptor)
                val name = getFileName() ?: "file"
                val pdfFile = File(context?.cacheDir, name)
                val outputStream = FileOutputStream(pdfFile)
                streamer.copyFile(inputStream, outputStream)
                Pair(name, pdfFile)
            }
        } catch (e: Exception) {
            null
        }
    }

    @SuppressLint("Range")
    private fun getFileName(): String? {
        return uri?.let { uri ->
            var result: String? = null
            if (uri.scheme == "content") {
                val cursor = contentResolver?.query(uri, null, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    cursor.close()
                }
            }
            if (result == null) {
                result = uri.path
                val cut = result!!.lastIndexOf('/')
                if (cut != -1) {
                    result = result.substring(cut + 1)
                }
            }
            result
        }
    }

}


