package com.atwa.filepicker.core

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.atwa.filepicker.result.FileMeta
import com.atwa.filepicker.result.ImageMeta
import com.atwa.filepicker.result.VideoMeta
import java.io.File
import java.lang.ref.WeakReference

interface FilePicker {

    /**
     * launching intent for picking image files.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onImagePicked Callback to receive the picker image result.
     */
    fun pickImage(onImagePicked: (ImageMeta?) -> Unit)

    /**
     * launching intent for picking images from gallery.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onImagePicked Callback to receive the picker gallery image result.
     */
    fun captureCameraImage(onImagePicked: (ImageMeta?) -> Unit)

    /**
     * launching intent for picking pdf files.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onPdfPicked Callback to receive the picker pdf result.
     */
    fun pickPdf(onPdfPicked: (FileMeta?) -> Unit)

    /**
     * launching intent for picking videos.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onVideoPicked Callback to receive the picker file result.
     */
    fun pickVideo(onVideoPicked: (VideoMeta?) -> Unit)

    /**
     * launching intent for picking files.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onFilePicked Callback to receive the picker file result.
     */
    fun pickFile(onFilePicked: (FileMeta?) -> Unit)

    /**
     * launching intent for picking files with possible initial directory path.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onFilePicked Callback to receive the picker file result.
     * @param initialDirectoryPath (Optional) initial directory to be opened for user to pick from.
     */
    fun pickFile(initialDirectoryPath:String,onFilePicked: (FileMeta?) -> Unit)

    companion object {
        @JvmStatic
        fun getInstance(activity: AppCompatActivity): FilePicker =
            ActivityFilePicker(WeakReference(activity))

        @JvmStatic
        fun getInstance(fragment: Fragment): FilePicker =
            FragmentFilePicker(WeakReference(fragment))
    }
}