package com.atwa.filepicker.core

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import java.io.File

interface FilePicker {

    /**
     * launching intent for picking image files.
     * This method should be called from activity and the result will be provided in the callback.
     *
     * Parameters:
     * @param activity Required to launch the picker intent.
     * @param onImagePicked Callback to receive the picker image result.
     */
    fun pickImage(activity: AppCompatActivity,onImagePicked: (Pair<Bitmap?, File?>?) -> Unit)

    /**
     * launching intent for picking pdf files.
     * This method should be called from activity and the result will be provided in the callback.
     *
     * Parameters:
     * @param activity Required to launch the picker intent.
     * @param onPdfPicked Callback to receive the picker pdf result.
     */
    fun pickPdf(activity: AppCompatActivity,onPdfPicked: (Pair<String?, File?>?) -> Unit)

    /**
     * launching intent for picking files.
     * This method should be called from activity and the result will be provided in the callback.
     *
     * Parameters:
     * @param activity Required to launch the picker intent.
     * @param onFilePicked Callback to receive the picker file result.
     */
    fun pickFile(activity: AppCompatActivity,onFilePicked: (Pair<String?, File?>?) -> Unit)

    companion object {
        @JvmStatic
        fun getInstance(): FilePicker = StorageFilePicker.instance
    }
}