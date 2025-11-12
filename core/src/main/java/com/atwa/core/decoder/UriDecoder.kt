package com.atwa.core.decoder

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
import com.atwa.core.stream.FileStreamer
import com.atwa.core.stream.Streamer
import com.atwa.core.threading.Executors
import com.atwa.result.FileResult
import com.atwa.result.ImageResult
import com.atwa.result.VideoResult
import java.io.*
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import kotlin.getValue


class UriDecoder(
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
            val file = File.createTempFile(timeStamp, fileSuffix, it.cacheDir)
            FileProvider.getUriForFile(
                it,
                "${it.packageName}.provider",
                file
            )
        }
    } catch (e: IOException) {
        println(e.message)
        null
    }

    override fun getStorageImage(
        imageUri: Uri?,
        onProgress: ((Float) -> Unit)?,
        onImagePicked: (ImageResult?) -> Unit,
    ) =
        executor.execute {
            decodeImage(
                uri = imageUri,
                onProgress = { handler?.post { onProgress?.invoke(it) } }
            ).also {
                handler?.post { onImagePicked(it) }
            }
        }

    override fun getStorageImages(
        imageUris: List<Uri>,
        onProgress: ((Int, Int, Float) -> Unit)?,
        onImagesPicked: (List<ImageResult?>) -> Unit,
    ) = executor.execute {
        imageUris.map { uri ->
            decodeImage(uri) { progress ->
                handler?.post { onProgress?.invoke(imageUris.indexOf(uri) + 1, imageUris.size, progress) }
            }
        }.also { uriList ->
            handler?.post { onImagesPicked(uriList) }
        }
    }


    override fun getCameraImage(
        imageUri: Uri?,
        onImagePicked: (ImageResult?) -> Unit,
    ) =
        executor.execute {
            decodeCameraImage(imageUri).also {
                handler?.post { onImagePicked(it) }
            }
        }

    override fun getCameraVideo(
        videoUri: Uri?,
        onVideoPicked: (VideoResult?) -> Unit,
    ) =
        executor.execute {
            decodeCameraVideo(videoUri).also {
                handler?.post { onVideoPicked(it) }

            }
        }

    override fun getStoragePDF(
        pdfUri: Uri?,
        onProgress: ((Float) -> Unit)?,
        onPdfPicked: (FileResult?) -> Unit,
    ) =
        executor.execute {
            decodeFile(
                uri = pdfUri,
                onProgress = { handler?.post { onProgress?.invoke(it) } })
                .also {
                    handler?.post { onPdfPicked(it) }
                }
        }

    override fun getStorageFile(
        fileUri: Uri?,
        onProgress: ((Float) -> Unit)?,
        onFilePicked: (FileResult?) -> Unit
    ) =
        executor.execute {
            decodeFile(
                uri = fileUri,
                onProgress = { handler?.post { onProgress?.invoke(it) } }
            ).also {
                handler?.post { onFilePicked(it) }
            }
        }

    override fun getStorageFiles(
        fileUris: List<Uri>,
        onProgress: ((Int, Int, Float) -> Unit)?,
        onFilesPicked: (List<FileResult?>) -> Unit,
    ) =
        executor.execute {
            fileUris.map { uri ->
                decodeFile(uri) { progress ->
                    handler?.post { onProgress?.invoke(fileUris.indexOf(uri), fileUris.size, progress) }
                }
            }.also { uriList ->
                handler?.post { onFilesPicked(uriList) }
            }
        }

    override fun getStorageVideo(
        videoUri: Uri?,
        onProgress: ((Float) -> Unit)?,
        onVideoPicked: (VideoResult?) -> Unit,
    ) =
        executor.execute {
            decodeVideo(
                uri = videoUri,
                onProgress = onProgress
            ).also {
                handler?.post { onVideoPicked(it) }
            }
        }

    private fun decodeImage(
        uri: Uri?,
        onProgress: ((Float) -> Unit)?
    ): ImageResult? {
        var pfd: ParcelFileDescriptor? = null
        return try {
            uri?.let {
                pfd = contentResolver?.openFileDescriptor(uri, "r")
                pfd?.let { fd ->
                    val inputStream = FileInputStream(fd.fileDescriptor)
                    val bitmap = decodeFileDescriptor(fd.fileDescriptor)
                    val meta = getFileResult(uri) ?: Pair("file", null)
                    val imageFile = File(context?.cacheDir, meta.first)
                    val outputStream = FileOutputStream(imageFile)
                    streamer.copyFile(inputStream, outputStream, meta.second.toBytes(), onProgress)
                    ImageResult(meta.first, meta.second, imageFile, bitmap)
                }
            }
        } catch (e: Exception) {
            println(e.message)
            null
        } finally {
            pfd?.close()
        }
    }

    private fun decodeCameraImage(uri: Uri?): ImageResult? {
        var pfd: ParcelFileDescriptor? = null
        return try {
            uri?.let {
                pfd = contentResolver?.openFileDescriptor(uri, "r")
                pfd?.let { fd ->
                    val originalBitmap = decodeFileDescriptor(fd.fileDescriptor)
                    val bitmap = BitmapRotation(context?.contentResolver, uri)
                        .run { rotateAccordingToOrientation(originalBitmap) }
                    val meta = getFileResult(uri) ?: Pair("file", null)
                    val imageFile = File(context?.cacheDir, meta.first)
                    ImageResult(meta.first, meta.second, imageFile, bitmap)
                }
            }
        } catch (e: Exception) {
            println(e.message)
            null
        } finally {
            pfd?.close()
        }
    }

    private fun decodeCameraVideo(uri: Uri?): VideoResult? {
        return try {
            uri?.let {
                val meta = getFileResult(uri) ?: Pair("file", null)
                val videoFile = File(context?.cacheDir, meta.first)

                val preview = ThumbnailUtils.createVideoThumbnail(
                    videoFile.path,
                    MediaStore.Video.Thumbnails.MINI_KIND
                )
                VideoResult(meta.first, videoFile.length().getFileSize(), videoFile, preview)
            }
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    private fun decodeVideo(
        uri: Uri?,
        onProgress: ((Float) -> Unit)?
    ): VideoResult? {
        var inputStream: InputStream? = null
        return try {
            uri?.let {
                inputStream = contentResolver?.openInputStream(uri)
                val fileName =
                    TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()
                        .plus(".mp4")
                val videoFile = File(context?.cacheDir, fileName)
                val outputStream = FileOutputStream(videoFile)
                inputStream?.let {
                    streamer.copyFile(
                        it,
                        outputStream,
                        videoFile.length().getFileSize().toBytes(),
                        onProgress
                    )
                }
                val preview = ThumbnailUtils.createVideoThumbnail(
                    videoFile.path,
                    MediaStore.Video.Thumbnails.MINI_KIND
                )
                VideoResult(fileName, videoFile.length().getFileSize(), videoFile, preview)
            }
        } catch (e: Exception) {
            println(e.message)
            null
        } finally {
            inputStream?.close()
        }
    }

    private fun decodeFile(
        uri: Uri?,
        onProgress: ((Float) -> Unit)? = null
    ): FileResult? {
        var pfd: ParcelFileDescriptor? = null
        return try {
            uri?.let {
                pfd = contentResolver?.openFileDescriptor(uri, "r")
                val inputStream = FileInputStream(pfd?.fileDescriptor)
                val meta = getFileResult(uri) ?: Pair("file", null)
                val file = File(context?.cacheDir, meta.first)
                val outputStream = FileOutputStream(file)
                streamer.copyFile(inputStream, outputStream, meta.second.toBytes(), onProgress)
                FileResult(meta.first, meta.second, file)
            }
        } catch (e: Exception) {
            println(e.message)
            null
        } finally {
            pfd?.close()

        }
    }

    @SuppressLint("Range")
    private fun getFileResult(uri: Uri): Pair<String, Int?>? {
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

private fun Int?.toBytes(): Long? {
    return this?.toLong()?.times(1000)
}


