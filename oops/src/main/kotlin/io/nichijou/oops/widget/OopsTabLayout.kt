package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.adjustAlpha


class OopsTabLayout : TabLayout, OopsLifecycleOwner {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.toolbarActiveColor.observe(this, Observer {
            this.setTabTextColors(it.adjustAlpha(.7f), it)
        })
        living.tabStateColor.observe(this, Observer {
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

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        attachOopsLife()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        handleOopsLifeStartOrStop(hasWindowFocus)
    }

    override fun onDetachedFromWindow() {
        detachOopsLife()
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