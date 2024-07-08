package com.atwa.filepicker.core

import android.content.Intent
import android.content.Intent.EXTRA_ALLOW_MULTIPLE
import android.provider.MediaStore

object PickIntent {

    internal val imageIntent by lazy {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
    }

    internal val imageMultiIntent by lazy {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).apply {
            putExtra(EXTRA_ALLOW_MULTIPLE, true)
        }
    }

    internal val captureCameraIntent
        get() =
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION and
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }

    internal val captureVideoIntent
        get() =
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
                addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION and
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }

    internal val pdfIntent by lazy {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
    }

    internal val videoIntent by lazy {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
    }

    internal val fileIntent by lazy { Intent(Intent.ACTION_GET_CONTENT) }

}