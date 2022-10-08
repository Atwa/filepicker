package com.atwa.filepicker.core

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.atwa.filepicker.decoder.Decoder
import com.atwa.filepicker.decoder.UriDecoder
import com.atwa.filepicker.request.FilePickerRequest
import com.atwa.filepicker.request.ImagePickerRequest
import com.atwa.filepicker.request.PdfPickerRequest
import com.atwa.filepicker.request.PickerRequest
import kotlinx.coroutines.launch
import java.io.File

internal class StorageFilePicker(private val activity: AppCompatActivity) : FilePicker {

    private lateinit var pickerRequest: PickerRequest
    private val decoder: Decoder by lazy { UriDecoder(activity.baseContext) }

    private val filePickerLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result?.data?.data?.let { processFile(it) }
        }

    override fun pickImage(onImagePicked: (Pair<Bitmap?, File?>?) -> Unit) {
        pickerRequest = ImagePickerRequest(decoder, onImagePicked)
        initialize()
    }

    override fun pickPdf(onPdfPicked: (Pair<String?, File?>?) -> Unit) {
        pickerRequest = PdfPickerRequest(decoder, onPdfPicked)
        initialize()
    }

    override fun pickFile(onFilePicked: (Pair<String?, File?>?) -> Unit) {
        pickerRequest = FilePickerRequest(decoder, onFilePicked)
        initialize()
    }

    private fun initialize() {
        filePickerLauncher.launch(pickerRequest.intent)
    }

    private fun processFile(uri: Uri) {
        activity.lifecycleScope.launch {
            pickerRequest.invokeCallback(uri)
        }
    }

}