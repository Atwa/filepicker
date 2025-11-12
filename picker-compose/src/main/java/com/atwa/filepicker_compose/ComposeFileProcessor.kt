package com.atwa.filepicker_compose

import android.content.Intent
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import com.atwa.core.decoder.Decoder
import com.atwa.core.intent.PickIntent.captureCameraIntent
import com.atwa.core.intent.PickIntent.captureVideoIntent
import com.atwa.core.intent.PickIntent.fileIntent
import com.atwa.core.intent.PickIntent.imageIntent
import com.atwa.core.intent.PickIntent.imageMultiIntent
import com.atwa.core.intent.PickIntent.pdfIntent
import com.atwa.core.intent.PickIntent.videoIntent
import com.atwa.result.FileResult
import com.atwa.result.ImageResult
import com.atwa.result.VideoResult

class ComposeFileProcessor(private val decoder: Decoder) : ComposeFilePicker {
    @Composable
    override fun imageLauncher(
        onProgress: ((Float) -> Unit)?,
        onImagePicked: (ImageResult?) -> Unit
    ) =
        imageIntent.onPick { uri ->
            decoder.getStorageImage(uri, onProgress, onImagePicked)
        }

    @Composable
    override fun multiImageLauncher(
        onProgress: ((Int, Int, Float) -> Unit)?,
        onImagesPicked: (List<ImageResult?>) -> Unit
    ) = imageMultiIntent.onMultiPick { uriList ->
        decoder.getStorageImages(uriList.filterNotNull(), onProgress, onImagesPicked)
    }

    @Composable
    override fun cameraImageLauncher(onImageCaptured: (ImageResult?) -> Unit): PickerLauncher {
        val photoURI = decoder.generateURI(isImageFile = true)
        val intent = captureCameraIntent.apply { putExtra(MediaStore.EXTRA_OUTPUT, photoURI) }
        return intent.onPick {
            decoder.getCameraImage(photoURI, onImageCaptured)
        }
    }

    @Composable
    override fun cameraVideoLauncher(onVideoCaptured: (VideoResult?) -> Unit): PickerLauncher {
        val videoURI = decoder.generateURI(isImageFile = false)
        val intent = captureVideoIntent.apply { putExtra(MediaStore.EXTRA_OUTPUT, videoURI) }
        return intent.onPick {
            decoder.getCameraVideo(videoURI, onVideoCaptured)
        }
    }

    @Composable
    override fun pdfLauncher(
        onProgress: ((Float) -> Unit)?,
        onPdfPicked: (FileResult?) -> Unit
    ) = pdfIntent.onPick { uri ->
        decoder.getStoragePDF(uri, onProgress, onPdfPicked)
    }

    @Composable
    override fun videoLauncher(
        onProgress: ((Float) -> Unit)?,
        onVideoPicked: (VideoResult?) -> Unit,
    ) = videoIntent.onPick { uri ->
        decoder.getStorageVideo(uri, onProgress, onVideoPicked)
    }

    @Composable
    override fun fileLauncher(
        onProgress: ((Float) -> Unit)?,
        onFilePicked: (FileResult?) -> Unit
    ) = fileLauncher("", "*/*", onProgress, onFilePicked)


    @Composable
    override fun mimeFileLauncher(
        mimeType: String,
        onProgress: ((Float) -> Unit)?,
        onFilePicked: (FileResult?) -> Unit
    ) = fileLauncher("", mimeType, onProgress, onFilePicked)


    @Composable
    override fun pathFileLauncher(
        initialDirectoryPath: String,
        onProgress: ((Float) -> Unit)?,
        onFilePicked: (FileResult?) -> Unit,
    ) = fileLauncher(initialDirectoryPath, "*/*", onProgress, onFilePicked)


    @Composable
    override fun fileLauncher(
        initialDirectoryPath: String,
        mimeType: String,
        onProgress: ((Float) -> Unit)?,
        onFilePicked: (FileResult?) -> Unit,
    ) = fileIntent.apply {
        if (initialDirectoryPath.isNotBlank())
            data = initialDirectoryPath.toUri()
        type = mimeType
    }.onPick { uri ->
        decoder.getStorageFile(uri, onProgress, onFilePicked)
    }

    @Composable
    override fun multiFilesLauncher(
        onProgress: ((Int, Int, Float) -> Unit)?,
        onFilesPicked: (List<FileResult?>) -> Unit
    ) = multiFilesLauncher("", "*/*", onProgress, onFilesPicked)

    @Composable
    override fun multiMimeFilesLauncher(
        mimeType: String,
        onProgress: ((Int, Int, Float) -> Unit)?,
        onFilesPicked: (List<FileResult?>) -> Unit,
    ) = multiFilesLauncher("", mimeType, onProgress, onFilesPicked)

    @Composable
    override fun multiPathFilesLauncher(
        initialDirectoryPath: String,
        onProgress: ((Int, Int, Float) -> Unit)?,
        onFilesPicked: (List<FileResult?>) -> Unit,
    ) = multiFilesLauncher(initialDirectoryPath, "*/*", onProgress, onFilesPicked)

    @Composable
    override fun multiFilesLauncher(
        initialDirectoryPath: String,
        mimeType: String,
        onProgress: ((Int, Int, Float) -> Unit)?,
        onFilesPicked: (List<FileResult?>) -> Unit,
    ) = fileIntent.apply {
        if (initialDirectoryPath.isNotBlank())
            data = initialDirectoryPath.toUri()
        type = mimeType
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
    }.onMultiPick { uriList ->
        decoder.getStorageFiles(uriList.filterNotNull(), onProgress, onFilesPicked)
    }

    override fun clear() {}

}
