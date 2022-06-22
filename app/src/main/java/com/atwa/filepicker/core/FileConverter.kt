package com.atwa.filepicker.core

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File

interface FileConverter {

    /**
     * Converts the image file to requestBody to be able to upload it to server
     *
     * Parameters:
     * @param file The image that was picked earlier in order to be uploaded to server.
     * @return RequestBody that should be used in the api call to upload the image.
     */
    fun imageToRequestBody(type: MediaType? = "image/jpeg".toMediaTypeOrNull(), file : File): RequestBody

    /**
     * Converts the image file to requestBody to be able to upload it to server
     *
     * Parameters:
     * @param file The Pdf that was picked earlier in order to be uploaded to server.
     * @return RequestBody that should be used in the api call to upload the pdf.
     */
    fun pdfToRequestBody(type: MediaType? = "application/pdf".toMediaTypeOrNull(),file: File): RequestBody

    /**
     * Converts the image file to requestBody to be able to upload it to server
     *
     * Parameters:
     * @param file The file that was picked earlier in order to be uploaded to server.
     * @return RequestBody that should be used in the api call to upload the file.
     */
    fun fileToRequestBody(type: MediaType? = "*/*".toMediaTypeOrNull(),file: File): RequestBody

    companion object {
        @JvmStatic
        fun getInstance(): FileConverter = RequestBodyConverter.instance
    }

}