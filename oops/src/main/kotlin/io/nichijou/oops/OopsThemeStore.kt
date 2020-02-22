package io.nichijou.oops

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.nichijou.oops.color.*
import io.nichijou.oops.ext.attrValueKey
import io.nichijou.oops.ext.collapsingToolbarDominantColorKey
import io.nichijou.oops.ext.mediateLiveData
import io.nichijou.oops.ext.mediateLiveDataNonNull
import io.nichijou.oops.live.DelegatePrefLive
import io.nichijou.oops.live.IntPrefLive

class OopsThemeStore : ViewModel() {

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
  val navBarColor by lazy {
    DelegatePrefLive(Oops.immed().prefs, Oops.immed()::navBarColor)
  }
  val navBarMode by lazy {
    DelegatePrefLive(Oops.immed().prefs, Oops.immed()::navBarMode)
  }
  val navBarDividerColor by lazy {
    DelegatePrefLive(Oops.immed().prefs, Oops.immed()::navBarDividerColor)
  }
  val swipeRefreshLayoutBackgroundColor by lazy {
    DelegatePrefLive(Oops.immed().prefs, Oops.immed()::swipeRefreshLayoutBackgroundColor)
  }
  val swipeRefreshLayoutSchemeColor by lazy {
    DelegatePrefLive(Oops.immed().prefs, Oops.immed()::swipeRefreshLayoutSchemeColor)
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
    return IntPrefLive(Oops.immed().prefs, tag.collapsingToolbarDominantColorKey())
  }

  val bottomNavStateColor by lazy {
    mediateLiveDataNonNull(bottomNavigationViewNormalColor, bottomNavigationViewSelectedColor, PairColor.live())
  }

  fun tabTextIconColor(tabTextColor: LiveData<Int>, tabSelectedTextColor: LiveData<Int>): LiveData<PairColor> {
    return mediateLiveDataNonNull(tabTextColor, tabSelectedTextColor, PairColor.live())
  }

  val statusBarStateColor by lazy {
    mediateLiveDataNonNull(statusBarColor, statusBarMode, BarStateColor.live())
  }
  val navBarStateColor by lazy {
    mediateLiveDataNonNull(navBarColor, navBarMode, BarStateColor.live())
  }
  val snackBarColor by lazy {
    mediateLiveDataNonNull(snackBarTextColor, snackBarActionColor, snackBarBackgroundColor, SnackbarColor.live())
  }

  fun materialButtonColor(textColor: LiveData<Int>?, strokeColor: LiveData<Int>?, rippleColor: LiveData<Int>?, bgColor: LiveData<Int>?, isDark: LiveData<Boolean>): LiveData<MaterialButtonColor> {
    return mediateLiveData(textColor, strokeColor, rippleColor, bgColor, isDark, MaterialButtonColor.live())
  }

  fun isDarkColor(color: LiveData<Int>): LiveData<IsDarkWithColor> {
    return mediateLiveDataNonNull(color, isDark, IsDarkWithColor.live())
  }

  fun collapsingToolbarStateColor(@NonNull domainColorTag: String, bgColor: LiveData<Int>): LiveData<CollapsingToolbarStateColor> {
    return mediateLiveDataNonNull(toolbarTitleColor, bgColor, statusBarColor, collapsingToolbarDominantColor(domainColorTag), CollapsingToolbarStateColor.live())
  }

  fun attrColor(attrValue: String): LiveData<Int>? {
    val signed = attrValue.attrValueKey()
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
      attrValue == "?android:navigationBarDividerColor" -> navBarDividerColor
      attrValue.contains("attr/") -> attrColor(attrValue) ?: fallback
      else -> fallback
    }
  }
}
