package io.nichijou.oops

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.nichijou.oops.color.*
import io.nichijou.oops.ext.liveMediator
import io.nichijou.oops.ext.oopsSignedAttrName

class OopsViewModel : ViewModel() {
    val theme by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::theme)
    }
    val isDark by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::isDark)
    }
    val colorAccent by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::colorAccent)
    }
    val colorPrimary by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::colorPrimary)
    }
    val colorPrimaryDark by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::colorPrimaryDark)
    }
    val statusBarColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::statusBarColor)
    }
    val statusBarMode by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::statusBarMode)
    }
    val swipeRefreshLayoutBackgroundColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::swipeRefreshLayoutBackgroundColor)
    }
    val swipeRefreshLayoutSchemeColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::swipeRefreshLayoutSchemeColor)
    }
    val navBarColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::navBarColor)
    }
    val windowBackground by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::windowBackground)
    }
    val textColorPrimary by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::textColorPrimary)
    }
    val textColorPrimaryInverse by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::textColorPrimaryInverse)
    }
    val textColorSecondary by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::textColorSecondary)
    }
    val textColorSecondaryInverse by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::textColorSecondaryInverse)
    }
    val toolbarActiveColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::toolbarActiveColor)
    }
    val toolbarInactiveColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::toolbarInactiveColor)
    }
    val toastTextColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::toastTextColor)
    }
    val toastBackgroundColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::toastBackgroundColor)
    }
    val snackBarTextColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::snackBarTextColor)
    }
    val snackBarActionColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::snackBarActionColor)
    }
    val snackBarBackgroundColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::snackBarBackgroundColor)
    }
    val navigationViewMode by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::navigationViewMode)
    }
    val tabLayoutBackgroundMode by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::tabLayoutBackgroundMode)
    }
    val tabLayoutIndicatorMode by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::tabLayoutIndicatorMode)
    }
    val bottomNavigationViewBackgroundMode by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::bottomNavigationViewBackgroundMode)
    }
    val bottomNavigationViewIconTextMode by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::bottomNavigationViewIconTextMode)
    }
    val collapsingToolbarDominantColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::collapsingToolbarDominantColor)
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
    val toastColor by lazy {
        liveMediator(toastTextColor, toastBackgroundColor, ToastColor.live())
    }

    fun isDarkColor(color: LiveData<Int>): LiveData<IsDarkColor> {
        return liveMediator(color, isDark, IsDarkColor.live())
    }

    fun collapsingToolbarStateColor(bgColor: LiveData<Int>): LiveData<CollapsingToolbarStateColor> {
        return liveMediator(toolbarActiveColor, bgColor, statusBarColor, collapsingToolbarDominantColor, CollapsingToolbarStateColor.live())
    }

    fun customAttrColor(attrName: String): LiveData<Int>? {
        val signed = attrName.oopsSignedAttrName()
        return if (Oops.immed().prefs.contains(signed))
            OopsIntPrefLive(Oops.immed().prefs, signed)
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
            attrName.contains("attr/") -> customAttrColor(attrName) ?: fallback
            else -> fallback
        }
    }
}