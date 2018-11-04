package io.nichijou.oops

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import io.nichijou.oops.color.*
import io.nichijou.oops.ext.liveMediator
import io.nichijou.oops.ext.oopsSignedAttrValue
import io.nichijou.oops.ext.oopsSignedCollapsingToolbarDominantColorKey

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
    val toolbarTitleColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::toolbarTitleColor)
    }
    val toolbarSubtitleColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::toolbarSubtitleColor)
    }
    val toolbarIconColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::toolbarIconColor)
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
    val tabLayoutTextColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::tabLayoutTextColor)
    }
    val tabLayoutSelectedTextColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::tabLayoutSelectedTextColor)
    }
    val bottomNavigationViewNormalColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::bottomNavigationViewNormalColor)
    }
    val bottomNavigationViewSelectedColor by lazy {
        OopsDelegateLive(Oops.immed().prefs, Oops.immed()::bottomNavigationViewSelectedColor)
    }

    fun collapsingToolbarDominantColor(@NonNull tag: String): OopsIntPrefLive {
        return OopsIntPrefLive(Oops.immed().prefs, tag.oopsSignedCollapsingToolbarDominantColorKey())
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
        liveMediator(snackBarTextColor, snackBarActionColor, snackBarBackgroundColor, SnackBarColor.live())
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
            OopsIntPrefLive(Oops.immed().prefs, signed)
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