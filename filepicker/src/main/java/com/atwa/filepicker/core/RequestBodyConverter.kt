package com.atwa.filepicker.core

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

internal class RequestBodyConverter : FileConverter {

    override fun toRequestBody(file: File,type: MediaType?): RequestBody {
        return file.asRequestBody(type)
    }

    companion object {
        @JvmStatic
        val instance: FileConverter by lazy { RequestBodyConverter() }
    }



}