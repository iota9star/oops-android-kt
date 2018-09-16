package io.nichijou.oops

import android.app.Application
import androidx.annotation.IdRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.nichijou.oops.ext.liveMediator
import io.nichijou.oops.ext.resId
import io.nichijou.oops.temp.*

class OopsViewModel(app: Application) : AndroidViewModel(app) {
    val theme by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::theme)
    }
    val isDark by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::isDark)
    }
    val colorAccent by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::colorAccent)
    }
    val colorPrimary by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::colorPrimary)
    }
    val colorPrimaryDark by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::colorPrimaryDark)
    }
    val statusBarColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::statusBarColor)
    }
    val statusBarMode by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::statusBarMode)
    }
    val swipeRefreshLayoutBackgroundColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::swipeRefreshLayoutBackgroundColor)
    }
    val navBarColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::navBarColor)
    }
    val windowBackground by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::windowBackground)
    }
    val textColorPrimary by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::textColorPrimary)
    }
    val textColorPrimaryInverse by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::textColorPrimaryInverse)
    }
    val textColorSecondary by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::textColorSecondary)
    }
    val textColorSecondaryInverse by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::textColorSecondaryInverse)
    }
    val iconTitleActiveColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::iconTitleActiveColor)
    }
    val iconTitleInactiveColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::iconTitleInactiveColor)
    }
    val snackBarTextColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::snackBarTextColor)
    }
    val snackBarActionColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::snackBarActionColor)
    }
    val snackBarBackgroundColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::snackBarBackgroundColor)
    }
    val cardViewBackgroundColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::cardViewBackgroundColor)
    }
    val navigationViewMode by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::navigationViewMode)
    }
    val tabLayoutBackgroundMode by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::tabLayoutBackgroundMode)
    }
    val tabLayoutIndicatorMode by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::tabLayoutIndicatorMode)
    }
    val bottomNavigationViewBackgroundMode by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::bottomNavigationViewBackgroundMode)
    }
    val bottomNavigationViewIconTextMode by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::bottomNavigationViewIconTextMode)
    }
    val collapsingToolbarColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::collapsingToolbarColor)
    }

    val activeColor by lazy {
        liveMediator(iconTitleActiveColor, iconTitleInactiveColor, ActiveColor.live())
    }

    val bottomNavStateColor by lazy {
        liveMediator(colorAccent, colorPrimary, colorPrimaryDark, isDark, bottomNavigationViewIconTextMode, bottomNavigationViewBackgroundMode, BottomNavStateColor.live())
    }

    val navStateColor by lazy {
        liveMediator(colorAccent, colorPrimary, isDark, navigationViewMode, NavStateColor.live())
    }

    val tabStateColor by lazy {
        liveMediator(colorAccent, colorPrimary, windowBackground, tabLayoutIndicatorMode, tabLayoutBackgroundMode, TabStateColor.live())
    }

    val statusBarStateColor by lazy {
        liveMediator(statusBarColor, statusBarMode, StatusBarStateColor.live())
    }

    fun isDarkColor(@IdRes resId: Int, fallback: LiveData<Int>): LiveData<IsDarkColor> {
        return liveMediator(live(resId, fallback)!!, isDark, IsDarkColor.live())
    }

    fun collapsingToolbarStateColor(@IdRes resId: Int): LiveData<CollapsingToolbarStateColor> {
        return liveMediator(iconTitleActiveColor, live(resId, colorPrimary)!!, statusBarColor, collapsingToolbarColor, CollapsingToolbarStateColor.live())
    }

    fun live(@IdRes resId: Int, fallback: LiveData<Int>? = null): LiveData<Int>? {
        val ctx = getApplication<Application>()
        return when (resId) {
            0 -> fallback
            ctx.resId(R.attr.colorAccent, 0),
            ctx.resId(android.R.attr.colorAccent, 0) -> colorAccent
            ctx.resId(R.attr.colorPrimary, 0),
            ctx.resId(android.R.attr.colorPrimary, 0) -> colorPrimary
            ctx.resId(R.attr.colorPrimaryDark, 0),
            ctx.resId(android.R.attr.colorPrimaryDark, 0) -> colorPrimaryDark
            ctx.resId(android.R.attr.statusBarColor, 0) -> statusBarColor
            ctx.resId(android.R.attr.windowBackground, 0) -> windowBackground
            ctx.resId(android.R.attr.textColorPrimary, 0) -> textColorPrimary
            ctx.resId(android.R.attr.textColorPrimaryInverse, 0) -> textColorPrimaryInverse
            ctx.resId(android.R.attr.textColorSecondary, 0) -> textColorSecondary
            ctx.resId(android.R.attr.textColorSecondaryInverse, 0) -> textColorSecondary
            else -> fallback
        }
    }
}