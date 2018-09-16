package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.R
import io.nichijou.oops.ext.adjustAlpha
import io.nichijou.oops.ext.colorRes
import io.nichijou.oops.ext.ctx
import io.nichijou.oops.ext.liveMediator
import io.nichijou.oops.temp.NavStateColor


class OopsNavigationView : NavigationView, OopsLiveProvider {

    constructor(context: Context) : super(context) {
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        registerOopsLive()
    }

    private var live: MediatorLiveData<NavStateColor>? = null

    override fun registerOopsLive() {
        val ctx = this.ctx()
        live = Oops.liveMediator(
                Oops.live(ctx, Oops.oops::colorAccent),
                Oops.live(ctx, Oops.oops::colorPrimary),
                Oops.live(ctx, Oops.oops::isDark),
                Oops.live(ctx, Oops.oops::navigationViewMode),
                NavStateColor.live())
        live!!.observe(ctx, Observer {
            when (it.mode) {
                NavigationViewTintMode.ACCENT -> updateColor(it.accent, it.isDark)
                NavigationViewTintMode.PRIMARY -> updateColor(it.primary, it.isDark)
            }
        })
    }

    override fun unregisterOopsLive() {
        live?.removeObservers(this.ctx())
        live = null
    }

    private fun updateColor(selectedColor: Int, isDark: Boolean) {
        val baseColor = if (isDark) Color.WHITE else Color.BLACK
        val unselectedIconColor = baseColor.adjustAlpha(0.54f)
        val unselectedTextColor = baseColor.adjustAlpha(0.87f)
        val selectedItemBgColor = this.ctx().colorRes(if (isDark) R.color.md_navigation_drawer_selected_dark else R.color.md_navigation_drawer_selected_light)
        val iconSl = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(unselectedIconColor, selectedColor))
        val textSl = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(unselectedTextColor, selectedColor))
        val bgDrawable = StateListDrawable()
        bgDrawable.addState(intArrayOf(android.R.attr.state_checked), ColorDrawable(selectedItemBgColor))

        this.itemTextColor = textSl
        this.itemIconTintList = iconSl
        this.itemBackground = bgDrawable
    }

    override fun onDetachedFromWindow() {
        unregisterOopsLive()
        super.onDetachedFromWindow()
    }
}

enum class NavigationViewTintMode {
    ACCENT, PRIMARY
}