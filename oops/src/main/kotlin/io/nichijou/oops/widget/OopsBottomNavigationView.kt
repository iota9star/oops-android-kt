package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.R
import io.nichijou.oops.ext.*
import io.nichijou.oops.temp.BottomNavStateColor


class OopsBottomNavigationView : BottomNavigationView, OopsLiveProvider {

    constructor(context: Context) : super(context) {
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        registerOopsLive()
    }

    private var live: MediatorLiveData<BottomNavStateColor>? = null

    override fun registerOopsLive() {
        val ctx = this.ctx()
        live = Oops.liveMediator(
                Oops.live(ctx, Oops.oops::colorAccent),
                Oops.live(ctx, Oops.oops::colorPrimary),
                Oops.live(ctx, Oops.oops::colorPrimaryDark),
                Oops.live(ctx, Oops.oops::isDark),
                Oops.live(ctx, Oops.oops::bottomNavigationViewIconTextMode),
                Oops.live(ctx, Oops.oops::bottomNavigationViewBackgroundMode),
                BottomNavStateColor.live())
        live!!.observe(ctx, Observer {
            val bgColor = when (it.backgroundMode) {
                BottomNavigationViewBackgroundMode.ACCENT -> it.accent
                BottomNavigationViewBackgroundMode.PRIMARY -> it.primary
                BottomNavigationViewBackgroundMode.PRIMARY_DARK -> it.primaryDark
                BottomNavigationViewBackgroundMode.AUTO -> this.ctx().colorRes(if (it.isDark) R.color.md_bottom_nav_default_dark_bg else R.color.md_bottom_nav_default_light_bg)
            }
            this.setBackgroundColor(bgColor)
            var iconTextColor = when (it.iconTextMode) {
                BottomNavigationViewIconTextMode.ACCENT -> it.accent
                BottomNavigationViewIconTextMode.PRIMARY -> it.primary
                BottomNavigationViewIconTextMode.AUTO -> Color.TRANSPARENT
            }
            if (iconTextColor == Color.TRANSPARENT) {
                iconTextColor = if (bgColor.isColorLight()) Color.BLACK else Color.WHITE
            }
            updateIconText(iconTextColor, bgColor)
        })
    }

    override fun unregisterOopsLive() {
        live?.removeObservers(this.ctx())
        live = null
    }

    private fun updateIconText(selectedColor: Int, bgColor: Int) {
        val baseColor = this.ctx().colorRes(if (bgColor.isColorLight()) R.color.md_icon_light else R.color.md_icon_dark)
        val unselectedIconTextColor = baseColor.adjustAlpha(.87f)
        val iconColor = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(unselectedIconTextColor, selectedColor))
        val textColor = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(unselectedIconTextColor, selectedColor))
        itemIconTintList = iconColor
        itemTextColor = textColor
    }

    override fun onDetachedFromWindow() {
        unregisterOopsLive()
        super.onDetachedFromWindow()
    }
}

enum class BottomNavigationViewBackgroundMode {
    ACCENT, PRIMARY, PRIMARY_DARK, AUTO
}

enum class BottomNavigationViewIconTextMode {
    ACCENT, PRIMARY, AUTO
}