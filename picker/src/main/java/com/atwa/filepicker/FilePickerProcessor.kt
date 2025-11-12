package com.atwa.filepicker

import android.content.Intent
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
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

class FilePickerProcessor(private val configuration: PickerConfiguration) : FilePicker {

    init {
        observeLifeCycle()
    }


    private fun observeLifeCycle() {
        configuration.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    configuration.lifecycle?.removeObserver(this)
                    configuration.clear()
                }
            }
        })
    }

    override fun pickImage(onProgress: ((Float) -> Unit)?, onImagePicked: (ImageResult?) -> Unit) {
        with(configuration) {
            imageIntent.onPick { uri ->
                decoder.getStorageImage(uri, onProgress, onImagePicked)
            }
        }
    }

    override fun pickMultiImage(
        onProgress: ((Int, Int, Float) -> Unit)?,
        onImagesPicked: (List<ImageResult?>) -> Unit,
    ) {
        with(configuration) {
            imageMultiIntent.onMultiPick { uriList ->
                decoder.getStorageImages(uriList.filterNotNull(), onProgress, onImagesPicked)
            }
        }
    }

    override fun captureCameraImage(onImagePicked: (ImageResult?) -> Unit) {
        with(configuration) {
            val photoURI = decoder.generateURI(isImageFile = true)
            captureCameraIntent.apply { putExtra(MediaStore.EXTRA_OUTPUT, photoURI) }.onPick {
                decoder.getCameraImage(photoURI, onImagePicked)
            }
        }
    }

    override fun captureCameraVideo(onVideoPicked: (VideoResult?) -> Unit) {
        with(configuration) {
            val videoURI = decoder.generateURI(isImageFile = false)
            captureVideoIntent.apply { putExtra(MediaStore.EXTRA_OUTPUT, videoURI) }.onPick {
                decoder.getCameraVideo(videoURI, onVideoPicked)
            }
        }
    }

    override fun pickPdf(onProgress: ((Float) -> Unit)?, onPdfPicked: (FileResult?) -> Unit) {
        with(configuration) {
            pdfIntent.onPick { uri ->
                decoder.getStoragePDF(uri, onProgress, onPdfPicked)
            }
        }
    }

    override fun pickVideo(onProgress: ((Float) -> Unit)?, onVideoPicked: (VideoResult?) -> Unit) {
        with(configuration) {
            videoIntent.onPick { uri ->
                decoder.getStorageVideo(uri, onProgress, onVideoPicked)
            }
        }
    }

    override fun pickFile(onProgress: ((Float) -> Unit)?, onFilePicked: (FileResult?) -> Unit) {
        pickFile("", "*/*", onProgress, onFilePicked)
    }

    override fun pickMimeFile(
        mimeType: String,
        onProgress: ((Float) -> Unit)?,
        onFilePicked: (FileResult?) -> Unit
    ) {
        pickFile("", mimeType, onProgress, onFilePicked)
    }

    override fun pickPathFile(
        initialDirectoryPath: String,
        onProgress: ((Float) -> Unit)?,
        onFilePicked: (FileResult?) -> Unit
    ) {
        pickFile(initialDirectoryPath, "*/*", onProgress, onFilePicked)
    }

    override fun pickFile(
        initialDirectoryPath: String,
        mimeType: String,
        onProgress: ((Float) -> Unit)?,
        onFilePicked: (FileResult?) -> Unit,
    ) {
        with(configuration) {
            fileIntent.apply {
                if (initialDirectoryPath.isNotBlank())
                    data = initialDirectoryPath.toUri()
                type = mimeType
            }.onPick { uri ->
                decoder.getStorageFile(uri, onProgress, onFilePicked)
            }
        }
    }

    override fun pickMultiFiles(
        onProgress: ((Int, Int, Float) -> Unit)?,
        onFilesPicked: (List<FileResult?>) -> Unit,
    ) {
        pickMultiFiles("", "*/*", onProgress, onFilesPicked)
    }

    override fun pickMultiMimeFiles(
        mimeType: String,
        onProgress: ((Int, Int, Float) -> Unit)?,
        onFilesPicked: (List<FileResult?>) -> Unit,
    ) {
        pickMultiFiles("", mimeType, onProgress, onFilesPicked)
    }

    override fun pickMultiPathFiles(
        initialDirectoryPath: String,
        onProgress: ((Int, Int, Float) -> Unit)?,
        onFilesPicked: (List<FileResult?>) -> Unit,
    ) {
        pickMultiFiles("", "*/*", onProgress, onFilesPicked)
    }

    override fun pickMultiFiles(
        initialDirectoryPath: String,
        mimeType: String,
        onProgress: ((Int, Int, Float) -> Unit)?,
        onFilesPicked: (List<FileResult?>) -> Unit,
    ) {
        with(configuration) {
            fileIntent.apply {
                if (initialDirectoryPath.isNotBlank())
                    data = initialDirectoryPath.toUri()
                type = mimeType
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }.onMultiPick { uriList ->
                decoder.getStorageFiles(uriList.filterNotNull(), onProgress, onFilesPicked)
            }
        }
    }

}