package com.atwa.filepicker.core

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File

interface FileConverter {

    val imageMediaType: MediaType?
        get() = "image/jpeg".toMediaTypeOrNull()

    val pdfMediaType: MediaType?
        get() = "application/pdf".toMediaTypeOrNull()

    val fileMediaType: MediaType?
        get() = "*/*".toMediaTypeOrNull()

    /**
     * Converts the file to requestBody to be able to upload it to server
     *
     * Parameters:
     * @param file The image that was picked earlier in order to be uploaded to server.
     * @return RequestBody that should be used in the api call to upload the file.
     */
    fun toRequestBody(file: File, type: MediaType?): RequestBody

    companion object {
        @JvmStatic
        fun getInstance(): FileConverter = RequestBodyConverter.instance
    }

}