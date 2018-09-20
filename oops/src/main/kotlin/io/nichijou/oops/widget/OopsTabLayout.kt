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


open class OopsTabLayout : TabLayout, OopsViewLifeAndLive {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun bindingLive() {
        ovm.iconTitleActiveColor.observe(this, Observer {
            this.setTabTextColors(it.adjustAlpha(0.7f), it)
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

    override fun getOopsViewModel(): OopsViewModel = ovm

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
        mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        } else {
            mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
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