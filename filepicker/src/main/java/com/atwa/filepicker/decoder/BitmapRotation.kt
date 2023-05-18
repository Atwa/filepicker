package com.atwa.filepicker.decoder

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import java.io.IOException
import java.io.InputStream

class BitmapRotation(private val contentResolver: ContentResolver?, val uri: Uri) {

    private fun getRotationAngle(): Float {
        return try {
            val exifInterface = getExifInterface() ?: return 0f
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                ExifInterface.ORIENTATION_NORMAL -> 0f
                ExifInterface.ORIENTATION_UNDEFINED -> -1f
                else -> -1f
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            0f
        }
    }

    private fun getExifInterface(): ExifInterface? {
        var inputStream: InputStream? = null
        try {
            val path = uri.toString()
            if (path.startsWith("file://")) {
                return ExifInterface(path)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (path.startsWith("content://")) {
                    inputStream = contentResolver?.openInputStream(uri)
                    return inputStream?.let { ExifInterface(it) }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return null
    }

    fun rotateAccordingToOrientation(bitmap: Bitmap?): Bitmap? {
        return bitmap?.let {
            val matrix = Matrix()
            matrix.postRotate(getRotationAngle())
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)
            Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )
        }
    }
}