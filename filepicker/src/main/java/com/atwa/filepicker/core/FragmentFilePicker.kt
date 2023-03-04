package com.atwa.filepicker.core

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

internal class FragmentFilePicker(private val fragment: WeakReference<Fragment>) : FilePicker {

    private lateinit var pickerRequest: PickerRequest
    private lateinit var cameraRequest: ImageCameraRequest
    private val decoder: Decoder by lazy {
        UriDecoder(
            fragment.get()?.requireActivity()?.applicationContext
        )
    }

    private val filePickerLauncher =
        fragment.get()
            ?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                result?.data?.data?.let { processFile(it) }
            }

    private val cameraCaptureLauncher =
        fragment.get()
            ?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

    override fun pickFile(initialDirectoryPath: String?,onFilePicked: (FileMeta?) -> Unit) {
        pickerRequest = FilePickerRequest(decoder, onFilePicked,initialDirectoryPath)
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
        fragment.get()?.lifecycleScope?.launchWhenResumed {
            pickerRequest.invokeCallback(uri)
        }
    }

    private fun processBitmap(bitmap: Bitmap) {
        fragment.get()?.lifecycleScope?.launchWhenResumed {
            cameraRequest.invokeCameraCallback(bitmap)
        }
    }

}