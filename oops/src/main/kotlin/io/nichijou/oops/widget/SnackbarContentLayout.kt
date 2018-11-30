package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.color.SnackbarColor
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.tint

@SuppressLint("RestrictedApi", "ViewConstructor")
internal class SnackbarContentLayout(context: Context, @Nullable attrs: AttributeSet?, private val enabledLiveNow: Boolean = true) : SnackbarContentLayout(context, attrs), OopsLifecycleOwner {

    private fun updateColor(color: SnackbarColor) {
        messageView.setTextColor(color.textColor)
        actionView.apply {
            val bg = background
            if (bg != null) {
                background = bg.tint(color.bgColor)
            } else {
                setBackgroundColor(color.bgColor)
            }
            setTextColor(color.actionColor)
        }
        setBackgroundColor(color.bgColor)
        val parent = this.parent
        if (parent is Snackbar.SnackbarLayout) {
            val background = parent.background
            if (background != null) {
                parent.background = background.tint(color.bgColor)
            } else {
                parent.setBackgroundColor(color.bgColor)
            }
        }
    }

    override fun liveInOops() {
        Oops.living(this.activity()).snackBarColor.observe(this, Observer(this::updateColor))
    }

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (enabledLiveNow) liveInOops()
        handleOopsLifeStart()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        handleOopsLifeStartOrStop(hasWindowFocus)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        handleOopsLifeStartOrStop(visibility == View.VISIBLE)
    }

    override fun onDetachedFromWindow() {
        handleOopsLifeDestroy()
        super.onDetachedFromWindow()
    }
}