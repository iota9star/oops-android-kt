package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.tintCircleBackground


@SuppressLint("ViewConstructor")
class SwipeRefreshLayout(context: Context, attrs: AttributeSet?, private val enabledLiveNow: Boolean = true) : SwipeRefreshLayout(context, attrs), OopsLifecycleOwner {

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.swipeRefreshLayoutBackgroundColor.observe(this, Observer(this::tintCircleBackground))
        living.swipeRefreshLayoutSchemeColor.observe(this, Observer { this.setColorSchemeColors(*it) })
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

    override fun onDetachedFromWindow() {
        handleOopsLifeDestroy()
        super.onDetachedFromWindow()
    }
}
