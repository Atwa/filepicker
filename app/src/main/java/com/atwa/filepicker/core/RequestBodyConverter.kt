package com.atwa.filepicker.core

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File

class RequestBodyConverter : FileConverter {

    override fun imageToRequestBody(type: MediaType?, file: File): RequestBody {
        return RequestBody.create(type, file)
    }

    override fun pdfToRequestBody(type: MediaType?, file: File): RequestBody {
        return RequestBody.create(type, file)
    }

    override fun fileToRequestBody(type: MediaType?, file: File): RequestBody {
        return RequestBody.create(type, file)
    }

    companion object {
        @JvmStatic
        val instance: FileConverter by lazy { RequestBodyConverter() }
    }

}