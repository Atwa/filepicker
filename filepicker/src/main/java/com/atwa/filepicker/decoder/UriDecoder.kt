package com.atwa.filepicker.decoder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory.decodeFileDescriptor
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Handler
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import com.atwa.filepicker.core.Executors
import com.atwa.filepicker.result.FileMeta
import com.atwa.filepicker.result.ImageMeta
import com.atwa.filepicker.result.VideoMeta
import com.atwa.filepicker.stream.FileStreamer
import com.atwa.filepicker.stream.Streamer
import java.io.*
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


internal class UriDecoder(
    private val context: Context?,
    private val handler: Handler? = null,
    private val streamer: Streamer = FileStreamer(),
    private val executor: Executor = Executors.executor,
) : Decoder {

    private val contentResolver by lazy { context?.contentResolver }

    override fun generateURI(isImageFile: Boolean): Uri? = try {
        context?.let {
            val timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()
            val fileSuffix = if (isImageFile) ".jpg" else ".mp4"
            val file = File.createTempFile(timeStamp, fileSuffix, context.cacheDir)
            FileProvider.getUriForFile(
                it,
                "${context.packageName}.provider",
                file
            )
        }
    } catch (ex: IOException) {
        null
    }

    override fun getStorageImage(imageUri: Uri?, onImagePicked: (ImageMeta?) -> Unit) =
        executor.execute {
            decodeImage(imageUri).also {
                handler?.post { onImagePicked(it) }
            }
        }

    override fun getStorageImages(
        imageUris: List<Uri>,
        onImagesPicked: (List<ImageMeta?>) -> Unit,
    ) = executor.execute {
        imageUris.map { uri -> decodeImage(uri) }.also { uriList ->
            handler?.post { onImagesPicked(uriList) }
        }
    }


    override fun getCameraImage(imageUri: Uri?, onImagePicked: (ImageMeta?) -> Unit) =
        executor.execute {
            decodeCameraImage(imageUri).also {
                handler?.post { onImagePicked(it) }
            }
        }

    override fun getCameraVideo(videoUri: Uri?, onVideoPicked: (VideoMeta?) -> Unit) =
        executor.execute {
            decodeCameraVideo(videoUri).also {
                handler?.post { onVideoPicked(it) }
            }
        }

    override fun getStoragePDF(pdfUri: Uri?, onPdfPicked: (FileMeta?) -> Unit) =
        executor.execute {
            decodeFile(pdfUri).also {
                handler?.post { onPdfPicked(it) }
            }
        }

    override fun getStorageFile(fileUri: Uri?, onFilePicked: (FileMeta?) -> Unit) =
        executor.execute {
            decodeFile(fileUri).also {
                handler?.post { onFilePicked(it) }
            }
        }

    override fun getStorageFiles(fileUris: List<Uri>, onFilesPicked: (List<FileMeta?>) -> Unit) =
        executor.execute {
            fileUris.map { uri -> decodeFile(uri) }.also { uriList ->
                handler?.post { onFilesPicked(uriList) }
            }
        }

    override fun getStorageVideo(videoUri: Uri?, onVideoPicked: (VideoMeta?) -> Unit) =
        executor.execute {
            decodeVideo(videoUri).also {
                handler?.post { onVideoPicked(it) }
            }
        }

    private fun decodeImage(uri: Uri?): ImageMeta? {
        var pfd: ParcelFileDescriptor? = null
        return try {
            uri?.let {
                pfd = contentResolver?.openFileDescriptor(uri, "r")
                pfd?.let { fd ->
                    val inputStream = FileInputStream(fd.fileDescriptor)
                    val bitmap = decodeFileDescriptor(fd.fileDescriptor)
                    val meta = getFileMeta(uri) ?: Pair("file", null)
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

    private fun decodeCameraImage(uri: Uri?): ImageMeta? {
        var pfd: ParcelFileDescriptor? = null
        return try {
            uri?.let {
                pfd = contentResolver?.openFileDescriptor(uri, "r")
                pfd?.let { fd ->
                    val originalBitmap = decodeFileDescriptor(fd.fileDescriptor)
                    val bitmap = BitmapRotation(context?.contentResolver, uri)
                        .run { rotateAccordingToOrientation(originalBitmap) }
                    val meta = getFileMeta(uri) ?: Pair("file", null)
                    val imageFile = File(context?.cacheDir, meta.first)
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

    private fun decodeCameraVideo(uri: Uri?): VideoMeta? {
        return try {
            uri?.let {
                val meta = getFileMeta(uri) ?: Pair("file", null)
                val videoFile = File(context?.cacheDir, meta.first)

                val preview = ThumbnailUtils.createVideoThumbnail(
                    videoFile.path,
                    MediaStore.Video.Thumbnails.MINI_KIND
                )
                VideoMeta(meta.first, videoFile.length().getFileSize(), videoFile, preview)
            }
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    private fun decodeVideo(uri: Uri?): VideoMeta? {
        var inputStream: InputStream? = null
        return try {
            uri?.let {
                inputStream = contentResolver?.openInputStream(uri)
                val fileName =
                    TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()
                        .plus(".mp4")
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

    private fun decodeFile(uri: Uri?): FileMeta? {
        var pfd: ParcelFileDescriptor? = null
        return try {
            uri?.let {
                pfd = contentResolver?.openFileDescriptor(uri, "r")
                val inputStream = FileInputStream(pfd?.fileDescriptor)
                val meta = getFileMeta(uri) ?: Pair("file", null)
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
    private fun getFileMeta(uri: Uri): Pair<String, Int?>? {
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
        return result
    }

}

private fun Long?.getFileSize(): Int? {
    return this?.div(1000)?.toInt()
}


