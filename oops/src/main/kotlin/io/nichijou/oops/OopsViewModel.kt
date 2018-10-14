package io.nichijou.oops

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.nichijou.oops.color.*
import io.nichijou.oops.ext.liveMediator
import io.nichijou.oops.ext.oopsSignedAttrName

class OopsViewModel(app: Application) : AndroidViewModel(app) {
    val theme by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::theme)
    }
    val isDark by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::isDark)
    }
    val colorAccent by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::colorAccent)
    }
    val colorPrimary by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::colorPrimary)
    }
    val colorPrimaryDark by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::colorPrimaryDark)
    }
    val statusBarColor by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::statusBarColor)
    }
    val statusBarMode by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::statusBarMode)
    }
    val swipeRefreshLayoutBackgroundColor by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::swipeRefreshLayoutBackgroundColor)
    }
    val swipeRefreshLayoutSchemeColor by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::swipeRefreshLayoutSchemeColor)
    }
    val navBarColor by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::navBarColor)
    }
    val windowBackground by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::windowBackground)
    }
    val textColorPrimary by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::textColorPrimary)
    }
    val textColorPrimaryInverse by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::textColorPrimaryInverse)
    }
    val textColorSecondary by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::textColorSecondary)
    }
    val textColorSecondaryInverse by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::textColorSecondaryInverse)
    }
    val toolbarActiveColor by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::toolbarActiveColor)
    }
    val toolbarInactiveColor by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::toolbarInactiveColor)
    }
    val snackBarTextColor by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::snackBarTextColor)
    }
    val snackBarActionColor by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::snackBarActionColor)
    }
    val snackBarBackgroundColor by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::snackBarBackgroundColor)
    }
    val navigationViewMode by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::navigationViewMode)
    }
    val tabLayoutBackgroundMode by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::tabLayoutBackgroundMode)
    }
    val tabLayoutIndicatorMode by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::tabLayoutIndicatorMode)
    }
    val bottomNavigationViewBackgroundMode by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::bottomNavigationViewBackgroundMode)
    }
    val bottomNavigationViewIconTextMode by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::bottomNavigationViewIconTextMode)
    }
    val collapsingToolbarColor by lazy {
        OopsDelegateLive(Oops.oops.prefs, Oops.oops::collapsingToolbarColor)
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

    fun customAttrColor(attrName: String): OopsSharedPreferencesLive<Int>? {
        return if (Oops.oops.prefs.contains(attrName))
            OopsSharedPreferencesLive(Oops.oops.prefs, attrName) {
                Oops.oops.prefs.getInt(attrName, 0)
            }
        else {
            null
        }
    }

    fun live(attrName: String?, fallback: LiveData<Int>? = null): LiveData<Int>? {
        return when {
            attrName.isNullOrBlank() -> fallback
            attrName == "?attr/colorPrimary"
                    || attrName == "?android:attr/colorPrimary" -> colorPrimary
            attrName == "?attr/colorPrimaryDark"
                    || attrName == "?android:attr/colorPrimaryDark" -> colorPrimaryDark
            attrName == "?attr/colorSecondary"
                    || attrName == "?android:attr/colorSecondary"
                    || attrName == "?attr/colorAccent"
                    || attrName == "?android:attr/colorAccent" -> colorAccent
            attrName == "?android:attr/windowBackground" -> windowBackground
            attrName == "?android:attr/textColorPrimary" -> textColorPrimary
            attrName == "?android:attr/textColorPrimaryInverse" -> textColorPrimaryInverse
            attrName == "?android:attr/textColorSecondary" -> textColorSecondary
            attrName == "?android:attr/textColorSecondaryInverse" -> textColorSecondaryInverse
            attrName == "?android:attr/statusBarColor" -> statusBarColor
            attrName == "?android:attr/navigationBarColor" -> navBarColor
            else -> fallback ?: customAttrColor(attrName.oopsSignedAttrName())
        }
    }
}