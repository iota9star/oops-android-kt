package io.nichijou.oops

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import io.nichijou.oops.color.CollapsingToolbarStateColor
import io.nichijou.oops.color.IsDarkWithColor
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.color.SnackbarColor
import io.nichijou.oops.color.StatusBarStateColor
import io.nichijou.oops.ext.liveMediator
import io.nichijou.oops.ext.oopsSignedAttrValue
import io.nichijou.oops.ext.oopsSignedCollapsingToolbarDominantColorKey

class OopsViewModel : ViewModel() {
    val theme by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::theme)
    }
    val isDark by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::isDark)
    }
    val colorAccent by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::colorAccent)
    }
    val colorPrimary by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::colorPrimary)
    }
    val colorPrimaryDark by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::colorPrimaryDark)
    }
    val statusBarColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::statusBarColor)
    }
    val statusBarMode by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::statusBarMode)
    }
    val swipeRefreshLayoutBackgroundColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::swipeRefreshLayoutBackgroundColor)
    }
    val swipeRefreshLayoutSchemeColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::swipeRefreshLayoutSchemeColor)
    }
    val navBarColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::navBarColor)
    }
    val windowBackground by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::windowBackground)
    }
    val textColorPrimary by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::textColorPrimary)
    }
    val textColorPrimaryInverse by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::textColorPrimaryInverse)
    }
    val textColorSecondary by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::textColorSecondary)
    }
    val textColorSecondaryInverse by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::textColorSecondaryInverse)
    }
    val toolbarTitleColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::toolbarTitleColor)
    }
    val toolbarSubtitleColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::toolbarSubtitleColor)
    }
    val toolbarIconColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::toolbarIconColor)
    }
    val snackBarTextColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::snackbarTextColor)
    }
    val snackBarActionColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::snackbarActionColor)
    }
    val snackBarBackgroundColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::snackbarBackgroundColor)
    }
    val navViewSelectedColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::navViewSelectedColor)
    }
    val tabLayoutTextColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::tabLayoutTextColor)
    }
    val tabLayoutSelectedTextColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::tabLayoutSelectedTextColor)
    }
    val bottomNavigationViewNormalColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::bottomNavigationViewNormalColor)
    }
    val bottomNavigationViewSelectedColor by lazy {
        DelegatePrefLive(Oops.immed().prefs, Oops.immed()::bottomNavigationViewSelectedColor)
    }

    fun collapsingToolbarDominantColor(@NonNull tag: String): IntPrefLive {
        return IntPrefLive(Oops.immed().prefs, tag.oopsSignedCollapsingToolbarDominantColorKey())
    }

    val bottomNavStateColor by lazy {
        liveMediator(bottomNavigationViewNormalColor, bottomNavigationViewSelectedColor, PairColor.live())
    }

    fun tabTextIconColor(tabTextColor: LiveData<Int>, tabSelectedTextColor: LiveData<Int>): MediatorLiveData<PairColor> {
        return liveMediator(tabTextColor, tabSelectedTextColor, PairColor.live())
    }

    val statusBarStateColor by lazy {
        liveMediator(statusBarColor, statusBarMode, StatusBarStateColor.live())
    }

    val snackBarColor by lazy {
        liveMediator(snackBarTextColor, snackBarActionColor, snackBarBackgroundColor, SnackbarColor.live())
    }

    fun isDarkColor(color: LiveData<Int>): LiveData<IsDarkWithColor> {
        return liveMediator(color, isDark, IsDarkWithColor.live())
    }

    fun collapsingToolbarStateColor(@NonNull domainColorTag: String, bgColor: LiveData<Int>): LiveData<CollapsingToolbarStateColor> {
        return liveMediator(toolbarTitleColor, bgColor, statusBarColor, collapsingToolbarDominantColor(domainColorTag), CollapsingToolbarStateColor.live())
    }

    fun attrColor(attrValue: String): LiveData<Int>? {
        val signed = attrValue.oopsSignedAttrValue()
        return if (Oops.immed().prefs.contains(signed))
            IntPrefLive(Oops.immed().prefs, signed)
        else {
            null
        }
    }

    fun live(attrValue: String?, fallback: LiveData<Int>? = null): LiveData<Int>? {
        return when {
            attrValue.isNullOrBlank() -> fallback
            attrValue == "?attr/colorPrimary"
                || attrValue == "?android:attr/colorPrimary" -> colorPrimary
            attrValue == "?attr/colorPrimaryDark"
                || attrValue == "?android:attr/colorPrimaryDark" -> colorPrimaryDark
            attrValue == "?attr/colorAccent"
                || attrValue == "?android:attr/colorAccent" -> colorAccent
            attrValue == "?android:attr/windowBackground" -> windowBackground
            attrValue == "?android:attr/textColorPrimary" -> textColorPrimary
            attrValue == "?android:attr/textColorPrimaryInverse" -> textColorPrimaryInverse
            attrValue == "?android:attr/textColorSecondary" -> textColorSecondary
            attrValue == "?android:attr/textColorSecondaryInverse" -> textColorSecondaryInverse
            attrValue == "?android:attr/statusBarColor" -> statusBarColor
            attrValue == "?android:attr/navigationBarColor" -> navBarColor
            attrValue.contains("attr/") -> attrColor(attrValue) ?: fallback
            else -> fallback
        }
    }
}