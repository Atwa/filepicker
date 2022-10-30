package com.atwa.filepicker.core

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.lang.ref.WeakReference

interface FilePicker {

    /**
     * launching intent for picking image files.
     * This method should be called from activity and the result will be provided in the callback.
     *
     * Parameters:
     * @param onImagePicked Callback to receive the picker image result.
     */
    fun pickImage(onImagePicked: (Pair<Bitmap?, File?>?) -> Unit)

    /**
     * launching intent for picking pdf files.
     * This method should be called from activity and the result will be provided in the callback.
     *
     * Parameters:
     * @param onPdfPicked Callback to receive the picker pdf result.
     */
    fun pickPdf(onPdfPicked: (Pair<String?, File?>?) -> Unit)

    /**
     * launching intent for picking files.
     * This method should be called from activity and the result will be provided in the callback.
     *
     * Parameters:
     * @param onFilePicked Callback to receive the picker file result.
     */
    fun pickFile(onFilePicked: (Pair<String?, File?>?) -> Unit)

    companion object {
        @JvmStatic
        fun getInstance(activity: AppCompatActivity): FilePicker =
            StorageFilePicker(WeakReference(activity))
    }
}