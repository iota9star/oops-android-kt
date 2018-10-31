package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.color.SnackbarColor
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.tint

@SuppressLint("RestrictedApi")
internal class OopsSnackBarContentLayout : SnackbarContentLayout, OopsViewLifeAndLive {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs)

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

    override fun howToLive() {
        oopsVM.snackbarColor.observe(this, Observer(this::updateColor))
    }

    override fun getOopsViewModel(): OopsViewModel = oopsVM

    private val oopsVM = ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)

    private val oopsLife: LifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = oopsLife

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startOopsLife()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        resumeOrPauseLife(hasWindowFocus)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        resumeOrPauseLife(visibility == View.VISIBLE)
    }

    override fun onDetachedFromWindow() {
        endOopsLife()
        super.onDetachedFromWindow()
    }
}