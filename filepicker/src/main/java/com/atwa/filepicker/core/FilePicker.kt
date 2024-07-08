package com.atwa.filepicker.core

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.atwa.filepicker.result.FileMeta
import com.atwa.filepicker.result.ImageMeta
import com.atwa.filepicker.result.VideoMeta
import java.lang.ref.WeakReference

interface FilePicker {

    /**
     * launching intent for picking image file.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onImagePicked Callback to receive the picker image result.
     */
    fun pickImage(onImagePicked: (ImageMeta?) -> Unit)

    /**
     * launching intent for picking multiple image files at once.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onImagesPicked Callback to receive the picker images result.
     */
    fun pickMultiImage(onImagesPicked: (List<ImageMeta?>) -> Unit)

    /**
     * launching intent for capturing images using device's camera.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onImagePicked Callback to receive the camera captured image result.
     */
    fun captureCameraImage(onImagePicked: (ImageMeta?) -> Unit)

    /**
     * launching intent for capturing videos using device's camera.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onVideoPicked Callback to receive the camera captured video result.
     */
    fun captureCameraVideo(onVideoPicked: (VideoMeta?) -> Unit)

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
     * launching intent for picking files filtered by a particular MIME type.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param mimeType MIME type to filter file types.
     * @param onFilePicked Callback to receive the picker file result.
     */
    fun pickMimeFile(mimeType: String, onFilePicked: (FileMeta?) -> Unit)

    /**
     * launching intent for picking files with initial directory path.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param initialDirectoryPath initial directory to be opened for user to pick from.
     * @param onFilePicked Callback to receive the picker file result.
     */
    fun pickPathFile(initialDirectoryPath: String, onFilePicked: (FileMeta?) -> Unit)

    /**
     * launching intent for picking files with initial directory path and filtered with MIME type.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onFilePicked Callback to receive the picker file result.
     * @param mimeType MIME type to filter file types.
     * @param initialDirectoryPath initial directory to be opened for user to pick from.
     */

    fun pickFile(initialDirectoryPath: String, mimeType: String, onFilePicked: (FileMeta?) -> Unit)


    /**
     * launching intent for picking multiple files.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onFilesPicked Callback to receive the picker multiple files result.
     */
    fun pickMultiFiles(onFilesPicked: (List<FileMeta?>) -> Unit)

    /**
     * launching intent for picking multiple files filtered by a particular MIME type.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param mimeType MIME type to filter file types.
     * @param onFilesPicked Callback to receive the picker file result.
     */
    fun pickMultiMimeFiles(mimeType: String, onFilesPicked: (List<FileMeta?>) -> Unit)

    /**
     * launching intent for picking multiple files with initial directory path.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param initialDirectoryPath initial directory to be opened for user to pick from.
     * @param onFilesPicked Callback to receive the picker file result.
     */
    fun pickMultiPathFiles(initialDirectoryPath: String, onFilesPicked: (List<FileMeta?>) -> Unit)


    /**
     * launching intent for picking multiple files with initial directory path and filtered with MIME type.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onFilesPicked Callback to receive the picker file result.
     * @param mimeType MIME type to filter file types.
     * @param initialDirectoryPath (Optional) initial directory to be opened for user to pick from.
     */

    fun pickMultiFiles(
        initialDirectoryPath: String,
        mimeType: String,
        onFilesPicked: (List<FileMeta?>) -> Unit,
    )

    companion object {
        @JvmStatic
        fun getInstance(activity: AppCompatActivity): FilePicker =
            FilePickerProcessor(ActivityConfiguration(WeakReference(activity)))

        @JvmStatic
        fun getInstance(fragment: Fragment): FilePicker =
            FilePickerProcessor(FragmentConfiguration(WeakReference(fragment)))
    }
}