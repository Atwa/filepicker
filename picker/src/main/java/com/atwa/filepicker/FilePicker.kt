package com.atwa.filepicker

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.atwa.result.FileResult
import com.atwa.result.ImageResult
import com.atwa.result.VideoResult
import java.lang.ref.WeakReference

interface FilePicker {

    /**
     * launching intent for picking image file.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onImagePicked Callback to receive the picker image result.
     */
    fun pickImage(
        onProgress: ((Float) -> Unit)? = null,
        onImagePicked: (ImageResult?) -> Unit
    )

    /**
     * launching intent for picking multiple image files at once.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onImagesPicked Callback to receive the picker images result.
     */
    fun pickMultiImage(
        onProgress: ((Int, Int, Float) -> Unit)? = null,
        onImagesPicked: (List<ImageResult?>) -> Unit
    )

    /**
     * launching intent for capturing images using device's camera.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onImagePicked Callback to receive the camera captured image result.
     */
    fun captureCameraImage(onImagePicked: (ImageResult?) -> Unit)

    /**
     * launching intent for capturing videos using device's camera.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onVideoPicked Callback to receive the camera captured video result.
     */
    fun captureCameraVideo(onVideoPicked: (VideoResult?) -> Unit)

    /**
     * launching intent for picking pdf files.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onPdfPicked Callback to receive the picker pdf result.
     */
    fun pickPdf(
        onProgress: ((Float) -> Unit)? = null,
        onPdfPicked: (FileResult?) -> Unit
    )

    /**
     * launching intent for picking videos.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onVideoPicked Callback to receive the picker file result.
     */
    fun pickVideo(
        onProgress: ((Float) -> Unit)? = null,
        onVideoPicked: (VideoResult?) -> Unit
    )

    /**
     * launching intent for picking files.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onFilePicked Callback to receive the picker file result.
     */
    fun pickFile(
        onProgress: ((Float) -> Unit)? = null,
        onFilePicked: (FileResult?) -> Unit
    )

    /**
     * launching intent for picking files filtered by a particular MIME type.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param mimeType MIME type to filter file types.
     * @param onFilePicked Callback to receive the picker file result.
     */
    fun pickMimeFile(
        mimeType: String,
        onProgress: ((Float) -> Unit)? = null,
        onFilePicked: (FileResult?) -> Unit
    )

    /**
     * launching intent for picking files with initial directory path.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param initialDirectoryPath initial directory to be opened for user to pick from.
     * @param onFilePicked Callback to receive the picker file result.
     */
    fun pickPathFile(
        initialDirectoryPath: String,
        onProgress: ((Float) -> Unit)? = null,
        onFilePicked: (FileResult?) -> Unit
    )

    /**
     * launching intent for picking files with initial directory path and filtered with MIME type.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param onFilePicked Callback to receive the picker file result.
     * @param mimeType MIME type to filter file types.
     * @param initialDirectoryPath initial directory to be opened for user to pick from.
     */

    fun pickFile(
        initialDirectoryPath: String,
        mimeType: String,
        onProgress: ((Float) -> Unit)? = null,
        onFilePicked: (FileResult?) -> Unit
    )

    /**
     * Launches an intent for picking multiple files.
     * This method should be called from an activity/fragment, and the result will be provided in the callback.
     *
     * Parameters:
     * @param onProgress Optional callback to track the progress of the file picking process.
     *                   It provides the current file index, total file count, and progress percentage.
     * @param onFilesPicked Callback to receive the result of the picked files as a list of `FileResult?`.
     */
    fun pickMultiFiles(
        onProgress: ((Int, Int, Float) -> Unit)? = null,
        onFilesPicked: (List<FileResult?>) -> Unit,
    )

    /**
     * Launches an intent for picking multiple files.
     * This method should be called from an activity/fragment, and the result will be provided in the callback.
     *
     * Parameters:
     * @param mimeType MIME type to filter file types.
     * @param onProgress Optional callback to track the progress of the file picking process.
     *                   It provides the current file index, total file count, and progress percentage.
     * @param onFilesPicked Callback to receive the result of the picked files as a list of `FileResult?`.
     */
    fun pickMultiMimeFiles(
        mimeType: String,
        onProgress: ((Int, Int, Float) -> Unit)? = null,
        onFilesPicked: (List<FileResult?>) -> Unit,
    )

    /**
     * launching intent for picking multiple files with initial directory path.
     * This method should be called from activity/fragment and the result will be provided in the callback.
     *
     * Parameters:
     * @param initialDirectoryPath initial directory to be opened for user to pick from.
     * @param onFilesPicked Callback to receive the picker file result.
     */
    fun pickMultiPathFiles(
        initialDirectoryPath: String,
        onProgress: ((Int, Int, Float) -> Unit)? = null,
        onFilesPicked: (List<FileResult?>) -> Unit,
    )


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
        onProgress: ((Int, Int, Float) -> Unit)? = null,
        onFilesPicked: (List<FileResult?>) -> Unit,
    )

    companion object {
        @JvmStatic
        fun getInstance(activity: ComponentActivity): FilePicker =
            FilePickerProcessor(ActivityConfiguration(WeakReference(activity)))

        @JvmStatic
        fun getInstance(fragment: Fragment): FilePicker =
            FilePickerProcessor(FragmentConfiguration(WeakReference(fragment)))
    }
}