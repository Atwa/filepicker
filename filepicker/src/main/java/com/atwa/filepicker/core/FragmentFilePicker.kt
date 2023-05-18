package com.atwa.filepicker.core

import android.content.Context
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.atwa.filepicker.BuildConfig
import com.atwa.filepicker.decoder.Decoder
import com.atwa.filepicker.decoder.UriDecoder
import com.atwa.filepicker.request.*
import com.atwa.filepicker.result.FileMeta
import com.atwa.filepicker.result.ImageMeta
import com.atwa.filepicker.result.VideoMeta
import java.io.IOException
import java.lang.ref.WeakReference

internal class FragmentFilePicker(private val fragment: WeakReference<Fragment>) : FilePicker {

    private lateinit var pickerRequest: PickerRequest
    private lateinit var imageCameraRequest: ImageCameraRequest
    private val decoder: Decoder by lazy {
        UriDecoder(fragment.get()?.requireActivity()?.applicationContext)
    }

    private val filePickerLauncher = fragment.get()
        ?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result?.data?.data?.let { processFile(it,pickerRequest) }
        }

    private val cameraCaptureLauncher = fragment.get()
        ?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            imageCameraRequest.photoURI?.let { processFile(it, imageCameraRequest) }
        }

    private fun initializeCameraRequest(context: Context, onImagePicked: (ImageMeta?) -> Unit) {
        imageCameraRequest = ImageCameraRequest(decoder, onImagePicked).apply {
            try {
                createImageFile(context)
            } catch (ex: IOException) {
                null
            }?.also { file ->
                photoURI = FileProvider.getUriForFile(
                    context,
                    "${BuildConfig.LIBRARY_PACKAGE_NAME}.provider",
                    file
                )
            }

        }
    }

    private fun observeLifeCycle() {
        fragment.get()?.viewLifecycleOwner?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    fragment.get()?.viewLifecycleOwner?.lifecycle?.removeObserver(this)
                    fragment.clear()
                }
            }
        })
    }

    override fun pickImage(onImagePicked: (ImageMeta?) -> Unit) {
        pickerRequest = ImagePickerRequest(decoder, onImagePicked)
        initialize()
    }

    override fun captureCameraImage(onImagePicked: (ImageMeta?) -> Unit) {
        fragment.get()?.requireActivity()?.applicationContext?.let { context ->
            observeLifeCycle()
            initializeCameraRequest(context, onImagePicked)
            imageCameraRequest.intent.let { intent ->
                cameraCaptureLauncher?.launch(intent)
            }
        }
    }

    override fun pickPdf(onPdfPicked: (FileMeta?) -> Unit) {
        pickerRequest = PdfPickerRequest(decoder, onPdfPicked)
        initialize()
    }

    override fun pickFile(onFilePicked: (FileMeta?) -> Unit) {
        pickerRequest = FilePickerRequest(decoder, onFilePicked)
        initialize()
    }

    override fun pickFile(initialDirectoryPath: String,onFilePicked: (FileMeta?) -> Unit) {
        pickerRequest = FilePickerRequest(decoder, onFilePicked,initialDirectoryPath)
        initialize()
    }

    override fun pickVideo(onVideoPicked: (VideoMeta?) -> Unit) {
        pickerRequest = VideoPickerRequest(decoder, onVideoPicked)
        initialize()
    }

    private fun initialize() {
        observeLifeCycle()
        filePickerLauncher?.launch(pickerRequest.intent)
    }

    private fun processFile(uri: Uri, request: PickerRequest) {
        fragment.get()?.lifecycleScope?.launchWhenResumed {
            request.invokeCallback(uri)
        }
    }

}