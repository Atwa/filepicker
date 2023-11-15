package com.atwa.filepicker.core

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.atwa.filepicker.decoder.Decoder
import com.atwa.filepicker.decoder.UriDecoder
import java.lang.ref.WeakReference

class ActivityConfiguration(
    private val activity: WeakReference<AppCompatActivity>,
) : PickerConfiguration {

    private var callback: ((uri: Uri?) -> Unit)? = null
    private val launcher = activity.get()
        ?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            callback?.invoke(result?.data?.data)
        }

    override val lifecycle = activity.get()?.lifecycle
    override val lifecycleScope = activity.get()?.lifecycleScope
    override val decoder: Decoder by lazy {
        UriDecoder(activity.get()?.applicationContext, Handler(Looper.getMainLooper()))
    }

    override fun Intent.onPick(callback: (uri: Uri?) -> Unit) {
        this@ActivityConfiguration.callback = callback
        launcher?.launch(this)
    }

    override fun clear() {
        activity.clear()
    }
}