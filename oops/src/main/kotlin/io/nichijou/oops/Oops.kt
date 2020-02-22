package io.nichijou.oops

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.SystemClock
import android.view.View
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import io.nichijou.oops.ext.*
import io.nichijou.oops.sp.*
import io.nichijou.oops.widget.BarMode
import kotlin.properties.ReadWriteProperty

@SuppressLint("CommitPrefEdits")
class Oops private constructor(val context: Context, override val prefs: SharedPreferences) : Pref() {
  override var transaction = false
  override var transactionTime: Long = 0

  override val prefsEditor: SharedPreferences.Editor = prefs.edit()

  private var viewInflaterFactory: ViewInflaterFactory? = null

  val isFirstTime: Boolean
    get() {
      val isFirst = prefs.getBoolean(KEY_IS_FIRST_TIME, true)
      prefsEditor.putBoolean(KEY_IS_FIRST_TIME, false).apply()
      return isFirst
    }
  var theme by intPref(0, KEY_THEME)

  fun themeSet(@StyleRes theme: Int): Oops {
    prefsEditor.putInt(KEY_THEME, theme)
    if (!transaction) prefsEditor.apply()
    return this
  }

  var isDark by booleanPref(false, KEY_IS_DARK)
  fun isDarkSet(isDark: Boolean): Oops {
    prefsEditor.putBoolean(KEY_IS_DARK, isDark)
    if (!transaction) prefsEditor.apply()
    return this
  }

  var colorAccent by intPref(0, KEY_COLOR_ACCENT)
  fun colorAccentSet(@ColorInt colorAccent: Int): Oops {
    prefsEditor.putInt(KEY_COLOR_ACCENT, colorAccent)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun colorAccentResSet(@ColorRes colorAccentRes: Int): Oops {
    return colorAccentSet(context.colorRes(colorAccentRes))
  }

  var colorPrimary by intPref(0, KEY_COLOR_PRIMARY)
  fun colorPrimarySet(@ColorInt colorPrimary: Int): Oops {
    prefsEditor.putInt(KEY_COLOR_PRIMARY, colorPrimary)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun colorPrimaryResSet(@ColorRes colorPrimaryRes: Int): Oops {
    return colorPrimarySet(context.colorRes(colorPrimaryRes))
  }

  var colorPrimaryDark by intPref(0, KEY_COLOR_PRIMARY_DARK)
  fun colorPrimaryDarkSet(@ColorInt colorPrimaryDark: Int): Oops {
    prefsEditor.putInt(KEY_COLOR_PRIMARY_DARK, colorPrimaryDark)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun colorPrimaryDarkResSet(@ColorRes colorPrimaryDarkRes: Int): Oops {
    return colorPrimaryDarkSet(context.colorRes(colorPrimaryDarkRes))
  }

  var statusBarColor by intPref(0, KEY_STATUS_BAR_COLOR)
  fun statusBarColorSet(@ColorInt statusBarColor: Int): Oops {
    prefsEditor.putInt(KEY_STATUS_BAR_COLOR, statusBarColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun statusBarColorResSet(@ColorRes statusBarColorRes: Int): Oops {
    return statusBarColorSet(context.colorRes(statusBarColorRes))
  }

  var statusBarMode by enumValuePref(BarMode.AUTO, KEY_STATUS_BAR_MODE)
  fun statusBarModeSet(statusBarMode: BarMode): Oops {
    prefsEditor.putString(KEY_STATUS_BAR_MODE, statusBarMode.name)
    if (!transaction) prefsEditor.apply()
    return this
  }

  var swipeRefreshLayoutBackgroundColor by intPref(Color.WHITE, KEY_SWIPE_REFRESH_LAYOUT_BACKGROUND_COLOR)
  fun swipeRefreshLayoutBackgroundColorSet(@ColorInt swipeRefreshLayoutBackgroundColor: Int): Oops {
    prefsEditor.putInt(KEY_SWIPE_REFRESH_LAYOUT_BACKGROUND_COLOR, swipeRefreshLayoutBackgroundColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun swipeRefreshLayoutBackgroundColorResSet(@ColorRes swipeRefreshLayoutBackgroundColorRes: Int): Oops {
    return swipeRefreshLayoutBackgroundColorSet(context.colorRes(swipeRefreshLayoutBackgroundColorRes))
  }

  var swipeRefreshLayoutSchemeColor by intArrayPref(intArrayOf(0x3cba54, 0xf4c20d, 0xdb3236, 0x4885ed), KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS)
  fun swipeRefreshLayoutSchemeColorSet(swipeRefreshLayoutSchemeColor: IntArray): Oops {
    if (swipeRefreshLayoutSchemeColor.isEmpty()) throw IllegalArgumentException("color res args size = 0")
    prefsEditor.putString(KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS, swipeRefreshLayoutSchemeColor.joinToString(","))
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun swipeRefreshLayoutSchemeColorsSet(@ColorInt vararg swipeRefreshLayoutSchemeColor: Int): Oops {
    if (swipeRefreshLayoutSchemeColor.isEmpty()) throw IllegalArgumentException("color res args size = 0")
    return swipeRefreshLayoutSchemeColorSet(swipeRefreshLayoutSchemeColor)
  }

  fun swipeRefreshLayoutSchemeColorResSet(swipeRefreshLayoutSchemeColorRes: IntArray): Oops {
    if (swipeRefreshLayoutSchemeColorRes.isEmpty()) throw IllegalArgumentException("color array size = 0")
    return swipeRefreshLayoutSchemeColorSet(swipeRefreshLayoutSchemeColorRes.map { context.colorRes(it) }.toIntArray())
  }

  fun swipeRefreshLayoutSchemeColorsResSet(@ColorRes vararg swipeRefreshLayoutSchemeColorRes: Int): Oops {
    if (swipeRefreshLayoutSchemeColorRes.isEmpty()) throw IllegalArgumentException("color res args size = 0")
    return swipeRefreshLayoutSchemeColorSet(swipeRefreshLayoutSchemeColorRes.map { context.colorRes(it) }.toIntArray())
  }

  var navBarColor by intPref(0, KEY_NAV_BAR_COLOR)
  fun navBarColorSet(@ColorInt navBarColor: Int): Oops {
    prefsEditor.putInt(KEY_NAV_BAR_COLOR, navBarColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  var navBarDividerColor by intPref(0, KEY_NAV_BAR_DIVIDER_COLOR)
  fun navBarDividerColorSet(@ColorInt dividerColor: Int): Oops {
    prefsEditor.putInt(KEY_NAV_BAR_DIVIDER_COLOR, dividerColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  var navBarMode by enumValuePref(BarMode.AUTO, KEY_NAV_BAR_MODE)
  fun navBarModeSet(navBarMode: BarMode): Oops {
    prefsEditor.putString(KEY_NAV_BAR_MODE, navBarMode.name)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun navBarColorResSet(@ColorRes navBarColorRes: Int): Oops {
    return navBarColorSet(context.colorRes(navBarColorRes))
  }

  var windowBackground by intPref(0xFAFAFA, KEY_WINDOW_BACKGROUND_COLOR)
  fun windowBackgroundSet(@ColorInt windowBackground: Int): Oops {
    prefsEditor.putInt(KEY_WINDOW_BACKGROUND_COLOR, windowBackground)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun windowBackgroundResSet(@ColorRes windowBackgroundRes: Int): Oops {
    return windowBackgroundSet(context.colorRes(windowBackgroundRes))
  }

  var textColorPrimary by intPref(0, KEY_PRIMARY_TEXT_COLOR)
  fun textColorPrimarySet(@ColorInt textColorPrimary: Int): Oops {
    prefsEditor.putInt(KEY_PRIMARY_TEXT_COLOR, textColorPrimary)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun textColorPrimaryResSet(@ColorRes textColorPrimaryRes: Int): Oops {
    return textColorPrimarySet(context.colorRes(textColorPrimaryRes))
  }

  var textColorPrimaryInverse by intPref(0, KEY_PRIMARY_TEXT_INVERSE_COLOR)
  fun textColorPrimaryInverseSet(@ColorInt textColorPrimaryInverse: Int): Oops {
    prefsEditor.putInt(KEY_PRIMARY_TEXT_INVERSE_COLOR, textColorPrimaryInverse)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun textColorPrimaryInverseResSet(@ColorRes textColorPrimaryInverseRes: Int): Oops {
    return textColorPrimaryInverseSet(context.colorRes(textColorPrimaryInverseRes))
  }

  var textColorSecondary by intPref(0, KEY_SECONDARY_TEXT_COLOR)
  fun textColorSecondarySet(@ColorInt textColorSecondary: Int): Oops {
    prefsEditor.putInt(KEY_SECONDARY_TEXT_COLOR, textColorSecondary)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun textColorSecondaryResSet(@ColorRes textColorSecondaryRes: Int): Oops {
    return textColorSecondarySet(context.colorRes(textColorSecondaryRes))
  }

  var textColorSecondaryInverse by intPref(0, KEY_SECONDARY_TEXT_INVERSE_COLOR)
  fun textColorSecondaryInverseSet(@ColorInt textColorSecondaryInverse: Int): Oops {
    prefsEditor.putInt(KEY_SECONDARY_TEXT_INVERSE_COLOR, textColorSecondaryInverse)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun textColorSecondaryInverseResSet(@ColorRes textColorSecondaryInverseRes: Int): Oops {
    return textColorSecondaryInverseSet(context.colorRes(textColorSecondaryInverseRes))
  }

  var toolbarTitleColor by intPref(0, KEY_TOOLBAR_TITLE_COLOR)
  fun toolbarTitleColorSet(@ColorInt toolbarTitleColor: Int): Oops {
    prefsEditor.putInt(KEY_TOOLBAR_TITLE_COLOR, toolbarTitleColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun toolbarTitleColorResSet(@ColorRes toolbarTitleColorRes: Int): Oops {
    return toolbarTitleColorSet(context.colorRes(toolbarTitleColorRes))
  }

  var toolbarSubtitleColor by intPref(0, KEY_TOOLBAR_SUBTITLE_COLOR)
  fun toolbarSubtitleColorSet(@ColorInt toolbarSubtitleColor: Int): Oops {
    prefsEditor.putInt(KEY_TOOLBAR_SUBTITLE_COLOR, toolbarSubtitleColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun toolbarSubtitleColorResSet(@ColorRes toolbarSubtitleColorRes: Int): Oops {
    return toolbarSubtitleColorSet(context.colorRes(toolbarSubtitleColorRes))
  }

  var toolbarIconColor by intPref(0, KEY_TOOLBAR_ICON_COLOR)
  fun toolbarIconColorSet(@ColorInt toolbarIconColor: Int): Oops {
    prefsEditor.putInt(KEY_TOOLBAR_ICON_COLOR, toolbarIconColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun toolbarIconColorResSet(@ColorRes toolbarIconColorRes: Int): Oops {
    return toolbarIconColorSet(context.colorRes(toolbarIconColorRes))
  }

  var snackbarTextColor by intPref(0, KEY_SNACK_BAR_TEXT_COLOR)
  fun snackbarTextColorSet(@ColorInt snackBarTextColor: Int): Oops {
    prefsEditor.putInt(KEY_SNACK_BAR_TEXT_COLOR, snackBarTextColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun snackbarTextColorResSet(@ColorRes snackBarTextColorRes: Int): Oops {
    return snackbarTextColorSet(context.colorRes(snackBarTextColorRes))
  }

  var snackbarActionColor by intPref(0, KEY_SNACK_BAR_ACTION_COLOR)
  fun snackbarActionColorSet(@ColorInt snackBarActionColor: Int): Oops {
    prefsEditor.putInt(KEY_SNACK_BAR_ACTION_COLOR, snackBarActionColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun snackbarActionColorResSet(@ColorRes snackBarActionColorRes: Int): Oops {
    return snackbarActionColorSet(context.colorRes(snackBarActionColorRes))
  }

  var snackbarBackgroundColor by intPref(0, KEY_SNACK_BAR_BACKGROUND_COLOR)
  fun snackbarBackgroundColorSet(@ColorInt snackBarBackgroundColor: Int): Oops {
    prefsEditor.putInt(KEY_SNACK_BAR_BACKGROUND_COLOR, snackBarBackgroundColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun snackbarBackgroundColorResSet(@ColorRes snackBarBackgroundColorRes: Int): Oops {
    return snackbarBackgroundColorSet(context.colorRes(snackBarBackgroundColorRes))
  }

  var navViewSelectedColor by intPref(0, KEY_NAV_VIEW_SELECTED_COLOR)
  fun navViewSelectedColorSet(@ColorInt navViewSelectedColor: Int): Oops {
    prefsEditor.putInt(KEY_NAV_VIEW_SELECTED_COLOR, navViewSelectedColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun navViewSelectedColorResSet(@ColorRes navViewSelectedColorRes: Int): Oops {
    return navViewSelectedColorSet(context.colorRes(navViewSelectedColorRes))
  }

  var tabLayoutTextColor by intPref(0, KEY_TAB_LAYOUT_TEXT_COLOR)
  fun tabLayoutTextColorSet(@ColorInt tabLayoutTextColor: Int): Oops {
    prefsEditor.putInt(KEY_TAB_LAYOUT_TEXT_COLOR, tabLayoutTextColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun tabLayoutTextColorResSet(@ColorRes tabLayoutTextColorRes: Int): Oops {
    return tabLayoutTextColorSet(context.colorRes(tabLayoutTextColorRes))
  }

  var tabLayoutSelectedTextColor by intPref(0, KEY_TAB_LAYOUT_SELECTED_TEXT_COLOR)
  fun tabLayoutSelectedTextColorSet(@ColorInt tabLayoutSelectedTextColor: Int): Oops {
    prefsEditor.putInt(KEY_TAB_LAYOUT_SELECTED_TEXT_COLOR, tabLayoutSelectedTextColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun tabLayoutSelectedTextColorResSet(@ColorRes tabLayoutSelectedTextColorRes: Int): Oops {
    return tabLayoutSelectedTextColorSet(context.colorRes(tabLayoutSelectedTextColorRes))
  }

  var bottomNavigationViewNormalColor by intPref(0, KEY_BOTTOM_NAV_NORMAL_COLOR)
  fun bottomNavigationViewNormalColorSet(@ColorInt bottomNavigationViewNormalColor: Int): Oops {
    prefsEditor.putInt(KEY_BOTTOM_NAV_NORMAL_COLOR, bottomNavigationViewNormalColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun bottomNavigationViewNormalColorResSet(@ColorRes bottomNavigationViewNormalColorRes: Int): Oops {
    return bottomNavigationViewNormalColorSet(context.colorRes(bottomNavigationViewNormalColorRes))
  }

  var bottomNavigationViewSelectedColor by intPref(0, KEY_BOTTOM_NAV_SELECTED_COLOR)
  fun bottomNavigationViewSelectedColorSet(@ColorInt bottomNavigationViewSelectedColor: Int): Oops {
    prefsEditor.putInt(KEY_BOTTOM_NAV_SELECTED_COLOR, bottomNavigationViewSelectedColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun bottomNavigationViewSelectedColorResSet(@ColorRes bottomNavigationViewSelectedColorRes: Int): Oops {
    return bottomNavigationViewSelectedColorSet(context.colorRes(bottomNavigationViewSelectedColorRes))
  }

  fun collapsingToolbarDominantColorGet(@NonNull tag: String): Int {
    return prefs.getInt(tag.collapsingToolbarDominantColorKey(), 0)
  }

  fun getCollapsingToolbarDominantColor(@NonNull tag: String): Int {
    return collapsingToolbarDominantColorGet(tag)
  }

  fun collapsingToolbarDominantColorSet(@NonNull tag: String, @ColorInt collapsingToolbarDominantColor: Int): Oops {
    prefsEditor.putInt(tag.collapsingToolbarDominantColorKey(), collapsingToolbarDominantColor)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun setCollapsingToolbarDominantColor(@NonNull tag: String, @ColorInt collapsingToolbarDominantColor: Int) {
    prefsEditor.putInt(tag.collapsingToolbarDominantColorKey(), collapsingToolbarDominantColor)
      .apply()
  }

  fun collapsingToolbarDominantColorResSet(@NonNull tag: String, @ColorRes collapsingToolbarDominantColorRes: Int): Oops {
    prefsEditor.putInt(tag.collapsingToolbarDominantColorKey(), context.colorRes(collapsingToolbarDominantColorRes))
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun setCollapsingToolbarDominantColorRes(@NonNull tag: String, @ColorRes collapsingToolbarDominantColorRes: Int) {
    prefsEditor.putInt(tag.collapsingToolbarDominantColorKey(), context.colorRes(collapsingToolbarDominantColorRes))
      .apply()
  }

  fun attrColorSet(context: Context, @AttrRes attrId: Int, @ColorInt color: Int): Oops {
    prefsEditor.putInt(context.attrValue(attrId).attrValueKey(), color)
    if (!transaction) prefsEditor.apply()
    return this
  }

  fun setAttrColor(context: Context, @AttrRes attrId: Int, @ColorInt color: Int) {
    prefsEditor.putInt(context.attrValue(attrId).attrValueKey(), color).apply()
  }

  fun attrColorResSet(context: Context, @AttrRes attrId: Int, @ColorRes colorRes: Int): Oops {
    return attrColorSet(context, attrId, context.colorRes(colorRes))
  }

  fun setAttrColorRes(context: Context, @AttrRes attrId: Int, @ColorRes colorRes: Int) {
    setAttrColor(context, attrId, context.colorRes(colorRes))
  }

  fun attrColorGet(context: Context, @AttrRes attrId: Int): Int {
    return attrColorGet(context.attrValue(attrId))
  }

  fun attrColorGet(attrValue: String): Int {
    return prefs.getInt(attrValue.attrValueKey(), 0)
  }

  fun getAttrColor(attrValue: String): Int {
    return attrColorGet(attrValue)
  }

  fun getAttrColor(context: Context, @AttrRes attrId: Int): Int {
    return attrColorGet(context.attrValue(attrId))
  }

  inline fun <reified T : AppCompatActivity> disableAutoStatusBarColor(disable: Boolean = true): Oops {
    return disableAutoStatusBarColor(T::class.java, disable)
  }

  fun disableAutoStatusBarColor(activity: AppCompatActivity, disable: Boolean = true): Oops {
    return disableAutoStatusBarColor(activity::class.java, disable)
  }

  fun disableAutoStatusBarColor(clazz: Class<out AppCompatActivity>, disable: Boolean = true): Oops {
    prefsEditor.putBoolean(clazz.name.disableStatusBarKey(), disable)
    if (!transaction) prefsEditor.apply()
    return this
  }

  inline fun <reified T : AppCompatActivity> disableAutoNavBarColor(disable: Boolean = true): Oops {
    return disableAutoNavBarColor(T::class.java, disable)
  }

  fun disableAutoNavBarColor(activity: AppCompatActivity, disable: Boolean = true): Oops {
    return disableAutoNavBarColor(activity::class.java, disable)
  }

  fun disableAutoNavBarColor(clazz: Class<out AppCompatActivity>, disable: Boolean = true): Oops {
    prefsEditor.putBoolean(clazz.name.disableNavBarKey(), disable)
    if (!transaction) prefsEditor.apply()
    return this
  }

  var rippleView: View? = null

  fun rippleViewSet(view: View): Oops {
    rippleView = view
    return this
  }

  var rippleAnimDuration: Long = 480

  fun rippleAnimDurationSet(@IntRange(from = 300, to = 1200) duration: Long): Oops {
    rippleAnimDuration = duration
    return this
  }

  private var rippleAnimation: RippleAnimation? = null

  private fun startRippleAnimation() {
    rippleAnimation = rippleView?.let {
      RippleAnimation.create(it)
    }?.apply {
      setDuration(rippleAnimDuration)
      setOnAnimationEndListener(object : RippleAnimation.OnAnimationEndListener {
        override fun onAnimationEnd() {
          rippleAnimation = null
        }
      })
      start()
    }
    rippleView = null
  }

  internal fun cancelRippleAnimation() {
    rippleAnimation?.cancel()
    rippleAnimation = null
  }

  private fun intPref(value: Int = 0, key: String): ReadWriteProperty<Oops, Int> = IntPref(value, key)
  private fun intArrayPref(value: IntArray, key: String): ReadWriteProperty<Oops, IntArray> = IntArrayPref(value, key)
  private fun booleanPref(value: Boolean = false, key: String): ReadWriteProperty<Oops, Boolean> = BooleanPref(value, key)
  private inline fun <reified T : Enum<*>> enumValuePref(default: T, key: String = ""): ReadWriteProperty<Oops, T> = EnumValuePref(T::class, default, key)

  private fun begin(): Oops {
    transaction = true
    transactionTime = SystemClock.uptimeMillis()
    return this
  }

  fun apply() {
    startRippleAnimation()
    prefsEditor.apply()
    transaction = false
  }

  private fun cancel(): Oops {
    transaction = false
    return this
  }

  companion object {
    @Volatile
    private lateinit var INSTANCE: Oops

    @JvmStatic
    fun immed() = INSTANCE.cancel()

    @JvmStatic
    fun bulk() = INSTANCE.begin()

    @JvmStatic
    fun bulk(block: Oops.() -> Unit) {
      try {
        INSTANCE.begin()
        INSTANCE.block()
        INSTANCE.apply()
      } catch (e: Exception) {
        INSTANCE.cancel()
        loge("Oops config not be save...", e)
        throw e
      }
    }

    @JvmOverloads
    @JvmStatic
    fun init(context: Context, prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)) {
      if (!::INSTANCE.isInitialized) {
        synchronized(this) {
          INSTANCE = Oops(context, prefs)
        }
      }
    }

    @JvmStatic
    @JvmOverloads
    fun attach(activity: AppCompatActivity, factory: ViewInflaterFactory? = null, viewInflatedProcessor: ViewInflatedProcessor? = null) {
      LayoutInflaterCompat.setFactory2(activity.layoutInflater, OopsInflaterFactory2(factory, viewInflatedProcessor))
      val theme = INSTANCE.theme
      if (theme > 0) {
        activity.setTheme(theme)
      }
      activity.attachOops(theme)
    }

    @JvmStatic
    fun setDefaultViewInflaterFactory(viewInflaterFactory: ViewInflaterFactory?) {
      INSTANCE.viewInflaterFactory = viewInflaterFactory
    }

    @JvmStatic
    fun getDefaultViewInflaterFactory() = INSTANCE.viewInflaterFactory

    @JvmStatic
    fun attrInLive(attrValue: String?): Boolean {
      val attrs = hashSetOf(
        "?attr/colorPrimary",
        "?android:attr/colorPrimary",
        "?attr/colorPrimaryDark",
        "?android:attr/colorPrimaryDark",
        "?attr/colorAccent",
        "?android:attr/colorAccent",
        "?android:attr/windowBackground",
        "?android:attr/textColorPrimary",
        "?android:attr/textColorPrimaryInverse",
        "?android:attr/textColorSecondary",
        "?android:attr/textColorSecondaryInverse",
        "?android:attr/statusBarColor",
        "?android:attr/navigationBarColor",
        "?android:navigationBarDividerColor"
      )
      return !attrValue.isNullOrBlank() && (attrs.contains(attrValue) || Oops.immed().prefs.contains(attrValue.attrValueKey()))
    }
  }
}
