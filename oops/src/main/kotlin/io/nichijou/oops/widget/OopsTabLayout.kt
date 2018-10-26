package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.adjustAlpha


class OopsTabLayout : TabLayout, OopsViewLifeAndLive {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun howToLive() {
        oopsVM.toolbarActiveColor.observe(this, Observer {
            this.setTabTextColors(it.adjustAlpha(.7f), it)
        })
        oopsVM.tabStateColor.observe(this, Observer {
            when (it.indicatorMode) {
                TabLayoutIndicatorMode.ACCENT -> this.setSelectedTabIndicatorColor(it.accent)
                TabLayoutIndicatorMode.PRIMARY -> this.setSelectedTabIndicatorColor(it.primary)
            }
            when (it.backgroundMode) {
                TabLayoutBackgroundMode.ACCENT -> this.setBackgroundColor(it.accent)
                TabLayoutBackgroundMode.PRIMARY -> this.setBackgroundColor(it.primary)
                TabLayoutBackgroundMode.WINDOW_BACKGROUND -> this.setBackgroundColor(it.windowBackground)
            }
        })
    }

    override fun getOopsViewModel(): OopsViewModel = oopsVM

    private val oopsVM by lazy {
        ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)
    }

    private val oopsLife: LifecycleRegistry by lazy {
        LifecycleRegistry(this)
    }

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

enum class TabLayoutIndicatorMode {
    ACCENT, PRIMARY
}

enum class TabLayoutBackgroundMode {
    ACCENT,
    PRIMARY,
    WINDOW_BACKGROUND
}