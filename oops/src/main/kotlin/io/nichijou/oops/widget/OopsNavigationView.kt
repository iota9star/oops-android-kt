package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.R
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.adjustAlpha
import io.nichijou.oops.ext.colorRes

@SuppressLint("RestrictedApi")
open class OopsNavigationView : NavigationView, OopsViewLifeAndLive {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun updateColor(selectedColor: Int, isDark: Boolean) {
        val baseColor = if (isDark) Color.WHITE else Color.BLACK
        val unselectedIconColor = baseColor.adjustAlpha(.54f)
        val unselectedTextColor = baseColor.adjustAlpha(.87f)
        val selectedItemBgColor = context.colorRes(if (isDark) R.color.md_navigation_drawer_selected_dark else R.color.md_navigation_drawer_selected_light)
        this.itemTextColor = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(unselectedTextColor, selectedColor))
        this.itemIconTintList = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(unselectedIconColor, selectedColor))
        this.itemBackground = StateListDrawable().apply {
            addState(intArrayOf(android.R.attr.state_checked), ColorDrawable(selectedItemBgColor))
        }
    }

    override fun bindingLive() {
        ovm.navStateColor.observe(this, Observer {
            when (it.mode) {
                NavigationViewTintMode.ACCENT -> updateColor(it.accent, it.isDark)
                NavigationViewTintMode.PRIMARY -> updateColor(it.primary, it.isDark)
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

enum class NavigationViewTintMode {
    ACCENT, PRIMARY
}