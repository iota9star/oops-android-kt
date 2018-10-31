package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.oopsTintCircleBackground


class OopsSwipeRefreshLayout(context: Context, attrs: AttributeSet?) : SwipeRefreshLayout(context, attrs), OopsViewLifeAndLive {

    override fun howToLive() {
        oopsVM.swipeRefreshLayoutBackgroundColor.observe(this, Observer(this::oopsTintCircleBackground))
        oopsVM.swipeRefreshLayoutSchemeColor.observe(this, Observer { this.setColorSchemeColors(*it) })
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

    override fun onDetachedFromWindow() {
        endOopsLife()
        super.onDetachedFromWindow()
    }
}
