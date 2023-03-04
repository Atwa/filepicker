package com.atwa.filepicker.core

import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.atwa.filepicker.decoder.Decoder
import com.atwa.filepicker.decoder.UriDecoder
import com.atwa.filepicker.request.*
import com.atwa.filepicker.request.FilePickerRequest
import com.atwa.filepicker.request.ImageCameraRequest
import com.atwa.filepicker.request.ImagePickerRequest
import com.atwa.filepicker.request.PdfPickerRequest
import com.atwa.filepicker.request.PickerRequest
import com.atwa.filepicker.result.FileMeta
import com.atwa.filepicker.result.ImageMeta
import com.atwa.filepicker.result.VideoMeta
import java.io.File
import java.lang.ref.WeakReference

internal class ActivityFilePicker(private val activity: WeakReference<AppCompatActivity>) : FilePicker {

    private lateinit var pickerRequest: PickerRequest
    private lateinit var cameraRequest: ImageCameraRequest
    private val decoder: Decoder by lazy { UriDecoder(activity.get()?.applicationContext) }

    private val filePickerLauncher =
        activity.get()?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result?.data?.data?.let { processFile(it) }
        }

    private val cameraCaptureLauncher =
        activity.get()?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            (result.data?.extras?.get("data") as? Bitmap)?.let { processBitmap(it) }
        }

    override fun pickImage(onImagePicked: (ImageMeta?) -> Unit) {
        pickerRequest = ImagePickerRequest(decoder, onImagePicked)
        initialize()
    }

    override fun captureCameraImage(onImagePicked: (ImageMeta?) -> Unit) {
        cameraRequest = ImageCameraRequest(decoder, onImagePicked)
        cameraCaptureLauncher?.launch(cameraRequest.intent)
    }

    override fun pickPdf(onPdfPicked: (FileMeta?) -> Unit) {
        pickerRequest = PdfPickerRequest(decoder, onPdfPicked)
        initialize()
    }

    override fun pickFile(onFilePicked: (FileMeta?) -> Unit) {
        pickerRequest = FilePickerRequest(decoder, onFilePicked)
        initialize()
    }

    override fun pickVideo(onVideoPicked: (VideoMeta?) -> Unit) {
        pickerRequest = VideoPickerRequest(decoder, onVideoPicked)
        initialize()
    }

    private fun initialize() {
        filePickerLauncher?.launch(pickerRequest.intent)
    }

    private fun processFile(uri: Uri) {
        activity.get()?.lifecycleScope?.launchWhenResumed {
            pickerRequest.invokeCallback(uri)
        }
    }

    private fun processBitmap(bitmap: Bitmap) {
        activity.get()?.lifecycleScope?.launchWhenResumed {
            cameraRequest.invokeCameraCallback(bitmap)
        }
    }

}