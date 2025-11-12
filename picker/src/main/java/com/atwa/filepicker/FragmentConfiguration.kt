package com.atwa.filepicker

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.atwa.core.decoder.Decoder
import com.atwa.core.decoder.UriDecoder
import java.lang.ref.WeakReference

class FragmentConfiguration(private val fragment: WeakReference<Fragment>) :
    PickerConfiguration {

    private var callback: ((uri: Uri?) -> Unit)? = null
    private var multiCallback: ((uriList: List<Uri?>) -> Unit)? = null
    private val launcher = fragment.get()
        ?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            callback?.invoke(result?.data?.data)
        }

    private val multiLauncher = fragment.get()
        ?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val uriList = arrayListOf<Uri?>()
            result?.data?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    uriList.add(clipData.getItemAt(i)?.uri)
                }
                multiCallback?.invoke(uriList.toMutableList())
                return@registerForActivityResult
            }
            multiCallback?.invoke(listOf(result?.data?.data))
        }

    override val lifecycle = fragment.get()?.lifecycle
    override val decoder: Decoder by lazy {
        UriDecoder(
            fragment.get()?.requireActivity()?.applicationContext,
            Handler(Looper.getMainLooper())
        )
    }

    override fun Intent.onPick(callback: (uri: Uri?) -> Unit) {
        this@FragmentConfiguration.callback = callback
        launcher?.launch(this)
    }

    override fun Intent.onMultiPick(callback: (uri: List<Uri?>) -> Unit) {
        this@FragmentConfiguration.multiCallback = callback
        multiLauncher?.launch(this)
    }

    override fun clear() {
        callback = null
        multiCallback = null
        fragment.clear()
    }
}