package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import io.nichijou.oops.OopsLifecycleOwner

@SuppressLint("ViewConstructor", "RestrictedApi")
internal class DialogTitle(context: Context, @Nullable attrs: AttributeSet?) : androidx.appcompat.widget.DialogTitle(context, attrs), OopsLifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handleOopsLifeStart()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        handleOopsLifeStartOrStop(hasWindowFocus)
    }

    override fun onDetachedFromWindow() {
        handleOopsLifeDestroy()
        super.onDetachedFromWindow()
    }
}
