package com.atwa.filepicker.core

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.atwa.filepicker.decoder.Decoder
import com.atwa.filepicker.decoder.UriDecoder
import java.lang.ref.WeakReference

class FragmentConfiguration(private val fragment: WeakReference<Fragment>) :
    PickerConfiguration {

    var callback: ((uri: Uri?) -> Unit)? = null
    private val launcher = fragment.get()
        ?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            callback?.invoke(result?.data?.data)
        }

    override val lifecycle = fragment.get()?.lifecycle
    override val lifecycleScope = fragment.get()?.lifecycleScope
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

    override fun clear() {
        fragment.clear()
    }
}