package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import io.nichijou.oops.OopsLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.adjustAlpha
import io.nichijou.oops.ext.tint


open class OopsTabLayout : TabLayout, OopsLifeAndLive {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun tintIcon(color: Int) {
        val sl = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_selected), intArrayOf(android.R.attr.state_selected)),
                intArrayOf(color.adjustAlpha(0.5f), color))
        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            if (tab != null && tab.icon != null) {
                tab.icon = tab.icon?.tint(sl)
            }
        }
    }

    override fun bindingLive() {
        ovm.activeColor.observe(this, Observer {
            this.tintIcon(it.active)
            this.setTabTextColors(it.active, it.inactive)
        })
        ovm.tabStateColor.observe(this, Observer {
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

    private val ovm by lazy {
        ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)
    }

    private val mViewLifecycleRegistry: LifecycleRegistry by lazy {
        LifecycleRegistry(this)
    }

    override fun getLifecycle(): Lifecycle = mViewLifecycleRegistry

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        bindingLive()
        mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        } else {
            mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        }
    }

    override fun onDetachedFromWindow() {
        mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
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