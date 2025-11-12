package com.atwa.filepicker_compose

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.atwa.core.decoder.UriDecoder
import com.atwa.result.FileResult
import com.atwa.result.ImageResult
import com.atwa.result.VideoResult

/**
 * An interface for picking files in Jetpack Compose.
 * It provides a set of composable functions that return a [ManagedActivityResultLauncher]
 * to handle different file picking scenarios.
 */
interface ComposeFilePicker {

    /**
     * Returns a [ManagedActivityResultLauncher] for picking an image file.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param onImagePicked Callback to receive the picked image result.
     */
    @Composable
    fun imageLauncher(
        onProgress: ((Float) -> Unit)? = null,
        onImagePicked: (ImageResult?) -> Unit
    ): PickerLauncher

    /**
     * Returns a [ManagedActivityResultLauncher] for picking multiple image files at once.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param onImagesPicked Callback to receive the picked images result.
     */
    @Composable
    fun multiImageLauncher(
        onProgress: ((Int, Int, Float) -> Unit)? = null,
        onImagesPicked: (List<ImageResult?>) -> Unit
    ): PickerLauncher

    /**
     * Returns a [ManagedActivityResultLauncher] for capturing an image using the device's camera.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param onImageCaptured Callback to receive the camera captured image result.
     */
    @Composable
    fun cameraImageLauncher(
        onImageCaptured: (ImageResult?) -> Unit
    ): PickerLauncher

    /**
     * Returns a [ManagedActivityResultLauncher] for capturing a video using the device's camera.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param onVideoCaptured Callback to receive the camera captured video result.
     */
    @Composable
    fun cameraVideoLauncher(
        onVideoCaptured: (VideoResult?) -> Unit
    ): PickerLauncher

    /**
     * Returns a [ManagedActivityResultLauncher] for picking a PDF file.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param onPdfPicked Callback to receive the picked pdf result.
     */
    @Composable
    fun pdfLauncher(
        onProgress: ((Float) -> Unit)? = null,
        onPdfPicked: (FileResult?) -> Unit
    ): PickerLauncher

    /**
     * Returns a [ManagedActivityResultLauncher] for picking a video file.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param onVideoPicked Callback to receive the picked video result.
     */
    @Composable
    fun videoLauncher(
        onProgress: ((Float) -> Unit)? = null,
        onVideoPicked: (VideoResult?) -> Unit
    ): PickerLauncher

    /**
     * Returns a [ManagedActivityResultLauncher] for picking a file.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param onFilePicked Callback to receive the picked file result.
     */
    @Composable
    fun fileLauncher(
        onProgress: ((Float) -> Unit)? = null,
        onFilePicked: (FileResult?) -> Unit
    ): PickerLauncher

    /**
     * Returns a [ManagedActivityResultLauncher] for picking a file of a specific MIME type.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param mimeType MIME type to filter file types.
     * @param onFilePicked Callback to receive the picked file result.
     */
    @Composable
    fun mimeFileLauncher(
        mimeType: String,
        onProgress: ((Float) -> Unit)? = null,
        onFilePicked: (FileResult?) -> Unit
    ): PickerLauncher

    /**
     * Returns a [ManagedActivityResultLauncher] for picking a file, starting in the specified directory.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param initialDirectoryPath initial directory to be opened for user to pick from.
     * @param onFilePicked Callback to receive the picked file result.
     */
    @Composable
    fun pathFileLauncher(
        initialDirectoryPath: String,
        onProgress: ((Float) -> Unit)? = null,
        onFilePicked: (FileResult?) -> Unit,
    ): PickerLauncher

    /**
     * Returns a [ManagedActivityResultLauncher] for picking a file of a specific MIME type, starting in the specified directory.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param onFilePicked Callback to receive the picked file result.
     * @param mimeType MIME type to filter file types.
     * @param initialDirectoryPath initial directory to be opened for user to pick from.
     */
    @Composable
    fun fileLauncher(
        initialDirectoryPath: String, mimeType: String,
        onProgress: ((Float) -> Unit)? = null,
        onFilePicked: (FileResult?) -> Unit,
    ): PickerLauncher


    /**
     * Returns a [ManagedActivityResultLauncher] for picking multiple files.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param onFilesPicked Callback to receive the picked multiple files result.
     */
    @Composable
    fun multiFilesLauncher(
        onProgress: ((Int, Int, Float) -> Unit)? = null,
        onFilesPicked: (List<FileResult?>) -> Unit
    ): PickerLauncher

    /**
     * Returns a [ManagedActivityResultLauncher] for picking multiple files of a specific MIME type.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param mimeType MIME type to filter file types.
     * @param onFilesPicked Callback to receive the picked files result.
     */
    @Composable
    fun multiMimeFilesLauncher(
        mimeType: String,
        onProgress: ((Int, Int, Float) -> Unit)? = null,
        onFilesPicked: (List<FileResult?>) -> Unit
    ): PickerLauncher

    /**
     * Returns a [ManagedActivityResultLauncher] for picking multiple files, starting in the specified directory.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param initialDirectoryPath initial directory to be opened for user to pick from.
     * @param onFilesPicked Callback to receive the picked files result.
     */
    @Composable
    fun multiPathFilesLauncher(
        initialDirectoryPath: String,
        onProgress: ((Int, Int, Float) -> Unit)? = null,
        onFilesPicked: (List<FileResult?>) -> Unit
    ): PickerLauncher


    /**
     * Returns a [ManagedActivityResultLauncher] for picking multiple files of a specific MIME type, starting in the specified directory.
     * This composable returns a launcher to start the file picking process from your UI.
     * The result will be provided in the callback.
     *
     * @param onFilesPicked Callback to receive the picked files result.
     * @param mimeType MIME type to filter file types.
     * @param initialDirectoryPath (Optional) initial directory to be opened for user to pick from.
     */

    @Composable
    fun multiFilesLauncher(
        initialDirectoryPath: String,
        mimeType: String,
        onProgress: ((Int, Int, Float) -> Unit)? = null,
        onFilesPicked: (List<FileResult?>) -> Unit
    ): PickerLauncher

    fun clear()

    companion object {
        @Composable
        fun getInstance(context: Context): ComposeFileProcessor {
            val processor = remember {
                ComposeFileProcessor(
                    UriDecoder(
                        context.applicationContext,
                        Handler(Looper.getMainLooper())
                    )
                )
            }
            DisposableEffect(context) {
                onDispose {
                    processor.clear()
                }
            }
            return processor
        }
    }
}
