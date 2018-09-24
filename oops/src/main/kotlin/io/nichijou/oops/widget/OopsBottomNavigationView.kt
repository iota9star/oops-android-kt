package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.R
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.adjustAlpha
import io.nichijou.oops.ext.colorRes
import io.nichijou.oops.ext.isColorLight


open class OopsBottomNavigationView : BottomNavigationView, OopsViewLifeAndLive {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun updateIconText(selectedColor: Int, bgColor: Int) {
        val baseColor = this.activity().colorRes(if (bgColor.isColorLight()) R.color.md_icon_light else R.color.md_icon_dark)
        val unselectedIconTextColor = baseColor.adjustAlpha(.87f)
        val iconColor = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(unselectedIconTextColor, selectedColor))
        val textColor = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(unselectedIconTextColor, selectedColor))
        itemIconTintList = iconColor
        itemTextColor = textColor
    }


    override fun bindingLive() {
        ovm.bottomNavStateColor.observe(this, Observer {
            val bgColor = when (it.backgroundMode) {
                BottomNavigationViewBackgroundMode.ACCENT -> it.accent
                BottomNavigationViewBackgroundMode.PRIMARY -> it.primary
                BottomNavigationViewBackgroundMode.PRIMARY_DARK -> it.primaryDark
                BottomNavigationViewBackgroundMode.AUTO -> context.colorRes(if (it.isDark) R.color.md_bottom_nav_default_dark_bg else R.color.md_bottom_nav_default_light_bg)
            }
            this.setBackgroundColor(bgColor)
            var iconTextColor = when (it.iconTextMode) {
                BottomNavigationViewIconTextMode.ACCENT -> it.accent
                BottomNavigationViewIconTextMode.PRIMARY -> it.primary
                BottomNavigationViewIconTextMode.AUTO -> -1
            }
            if (iconTextColor == -1) {
                iconTextColor = if (bgColor.isColorLight()) Color.BLACK else Color.WHITE
            }
            updateIconText(iconTextColor, bgColor)
        })
    }

    private val ovm by lazy {
        ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)
    }

    override fun getOopsViewModel(): OopsViewModel = ovm

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

enum class BottomNavigationViewBackgroundMode {
    ACCENT, PRIMARY, PRIMARY_DARK, AUTO
}

enum class BottomNavigationViewIconTextMode {
    ACCENT, PRIMARY, AUTO
}