package io.nichijou.oops

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.nichijou.oops.ext.liveMediator
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
    val swipeRefreshLayoutSchemeColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::swipeRefreshLayoutSchemeColor)
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
    val toolbarActiveColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::toolbarActiveColor)
    }
    val toolbarInactiveColor by lazy {
        OopsLive(Oops.oops.prefs, Oops.oops::toolbarInactiveColor)
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

    val toolbarColor by lazy {
        liveMediator(toolbarActiveColor, toolbarInactiveColor, ActiveColor.live())
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

    fun isDarkColor(color: LiveData<Int>): LiveData<IsDarkColor> {
        return liveMediator(color, isDark, IsDarkColor.live())
    }

    fun collapsingToolbarStateColor(bgColor: LiveData<Int>): LiveData<CollapsingToolbarStateColor> {
        return liveMediator(toolbarActiveColor, bgColor, statusBarColor, collapsingToolbarColor, CollapsingToolbarStateColor.live())
    }

    fun live(attrName: String?, fallback: LiveData<Int>? = null): LiveData<Int>? {
        return when (attrName) {
            "", null -> fallback
            "?attr/colorPrimary", "?android:attr/colorPrimary" -> colorPrimary
            "?attr/colorPrimaryDark", "?android:attr/colorPrimaryDark" -> colorPrimaryDark
            "?attr/colorSecondary", "?android:attr/colorSecondary", "?attr/colorAccent", "?android:attr/colorAccent" -> colorAccent
            "?android:attr/windowBackground" -> windowBackground
            "?android:attr/textColorPrimary" -> textColorPrimary
            "?android:attr/textColorPrimaryInverse" -> textColorPrimaryInverse
            "?android:attr/textColorSecondary" -> textColorSecondary
            "?android:attr/textColorSecondaryInverse" -> textColorSecondaryInverse
            else -> fallback
        }
    }
}