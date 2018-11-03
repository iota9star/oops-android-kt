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
import com.google.android.material.navigation.NavigationView
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.R
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.adjustAlpha
import io.nichijou.oops.ext.colorRes

@SuppressLint("RestrictedApi")
class OopsNavigationView : NavigationView, OopsLifecycleOwner {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

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

    override fun liveInOops() {
        Oops.living(this.activity()).navStateColor.observe(this, Observer {
            when (it.mode) {
                NavigationViewTintMode.ACCENT -> updateColor(it.accent, it.isDark)
                NavigationViewTintMode.PRIMARY -> updateColor(it.primary, it.isDark)
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

enum class NavigationViewTintMode {
    ACCENT, PRIMARY
}