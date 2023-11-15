package com.atwa.filepicker.core

import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.atwa.filepicker.core.PickIntent.captureIntent
import com.atwa.filepicker.core.PickIntent.fileIntent
import com.atwa.filepicker.core.PickIntent.imageIntent
import com.atwa.filepicker.core.PickIntent.pdfIntent
import com.atwa.filepicker.core.PickIntent.videoIntent
import com.atwa.filepicker.result.FileMeta
import com.atwa.filepicker.result.ImageMeta
import com.atwa.filepicker.result.VideoMeta

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

    override fun pickImage(onImagePicked: (ImageMeta?) -> Unit) {
        with(configuration) {
            imageIntent.onPick { uri ->
                decoder.getStorageImage(uri, onImagePicked)
            }
        }
    }

    override fun captureCameraImage(onImagePicked: (ImageMeta?) -> Unit) {
        with(configuration) {
            val photoURI = decoder.createFile()
            captureIntent.apply { putExtra(MediaStore.EXTRA_OUTPUT, photoURI) }.onPick {
                decoder.getCameraImage(photoURI, onImagePicked)
            }
        }
    }

    override fun pickPdf(onPdfPicked: (FileMeta?) -> Unit) {
        with(configuration) {
            pdfIntent.onPick { uri ->
                decoder.getStoragePDF(uri, onPdfPicked)
            }
        }
    }

    override fun pickVideo(onVideoPicked: (VideoMeta?) -> Unit) {
        with(configuration) {
            videoIntent.onPick { uri ->
                decoder.getStorageVideo(uri, onVideoPicked)
            }
        }
    }

    override fun pickFile(onFilePicked: (FileMeta?) -> Unit) {
        pickFile("", onFilePicked)
    }

    override fun pickFile(initialDirectoryPath: String, onFilePicked: (FileMeta?) -> Unit) {
        with(configuration) {
            fileIntent.apply {
                if (initialDirectoryPath.isNotBlank())
                    data = initialDirectoryPath.toUri()
                type = "*/*"
            }.onPick { uri ->
                decoder.getStorageFile(uri, onFilePicked)
            }
        }
    }
}