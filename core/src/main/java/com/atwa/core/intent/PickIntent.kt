package com.atwa.core.intent

import android.content.Intent
import android.provider.MediaStore

object PickIntent {

    val imageIntent by lazy {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
    }

    val imageMultiIntent by lazy {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
    }

    val captureCameraIntent
        get() =
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION and
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }

    val captureVideoIntent
        get() =
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
                addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION and
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }

    val pdfIntent by lazy {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
    }

    val videoIntent by lazy {
        Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
    }

    val fileIntent by lazy { Intent(Intent.ACTION_GET_CONTENT) }

}