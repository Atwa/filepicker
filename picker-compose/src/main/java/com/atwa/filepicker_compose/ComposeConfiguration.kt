package com.atwa.filepicker_compose

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun Intent.onPick(callback: (uri: Uri?) -> Unit) =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            callback(result.data?.data)
        }
    ).toPickerLauncher(this)

@Composable
fun Intent.onMultiPick(callback: (uri: List<Uri?>) -> Unit) =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val uriList = arrayListOf<Uri?>()
            result.data?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    uriList.add(clipData.getItemAt(i)?.uri)
                }
                callback.invoke(uriList.toMutableList())
                return@rememberLauncherForActivityResult
            }
            callback.invoke(listOf(result.data?.data))
        }
    ).toPickerLauncher(this)