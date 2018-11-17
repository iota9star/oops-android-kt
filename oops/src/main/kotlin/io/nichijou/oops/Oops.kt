package io.nichijou.oops

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.SystemClock
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.ext.attachOops
import io.nichijou.oops.ext.attrValue
import io.nichijou.oops.ext.colorRes
import io.nichijou.oops.ext.loge
import io.nichijou.oops.ext.oopsSignedAttrValue
import io.nichijou.oops.ext.oopsSignedCollapsingToolbarDominantColorKey
import io.nichijou.oops.ext.oopsSignedNavBarColorKey
import io.nichijou.oops.ext.oopsSignedStatusBarColorKey
import io.nichijou.oops.pref.BooleanPref
import io.nichijou.oops.pref.EnumValuePref
import io.nichijou.oops.pref.IntArrayPref
import io.nichijou.oops.pref.IntPref
import io.nichijou.oops.widget.StatusBarMode
import kotlin.properties.ReadWriteProperty


@SuppressLint("CommitPrefEdits")
class Oops private constructor(val context: Context) {

    private var layoutInflaterFactory: LayoutInflaterFactory? = null

    val isFirstTime: Boolean
        get() {
            val isFirst = prefs.getBoolean(OopsPrefsKey.KEY_IS_FIRST_TIME, true)
            prefsEditor.putBoolean(OopsPrefsKey.KEY_IS_FIRST_TIME, false).apply()
            return isFirst
        }

    var theme by intPref(0, OopsPrefsKey.KEY_THEME)

    fun themeSet(@StyleRes theme: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_THEME, theme)
        if (!transaction) prefsEditor.apply()
        return this
    }

    var isDark by booleanPref(false, OopsPrefsKey.KEY_IS_DARK)
    fun isDarkSet(isDark: Boolean): Oops {
        prefsEditor.putBoolean(OopsPrefsKey.KEY_IS_DARK, isDark)
        if (!transaction) prefsEditor.apply()
        return this
    }

    var colorAccent by intPref(0, OopsPrefsKey.KEY_COLOR_ACCENT)
    fun colorAccentSet(@ColorInt colorAccent: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLOR_ACCENT, colorAccent)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun colorAccentResSet(@ColorRes colorAccentRes: Int): Oops {
        return colorAccentSet(context.colorRes(colorAccentRes))
    }

    var colorPrimary by intPref(0, OopsPrefsKey.KEY_COLOR_PRIMARY)
    fun colorPrimarySet(@ColorInt colorPrimary: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLOR_PRIMARY, colorPrimary)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun colorPrimaryResSet(@ColorRes colorPrimaryRes: Int): Oops {
        return colorPrimarySet(context.colorRes(colorPrimaryRes))
    }

    var colorPrimaryDark by intPref(0, OopsPrefsKey.KEY_COLOR_PRIMARY_DARK)
    fun colorPrimaryDarkSet(@ColorInt colorPrimaryDark: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLOR_PRIMARY_DARK, colorPrimaryDark)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun colorPrimaryDarkResSet(@ColorRes colorPrimaryDarkRes: Int): Oops {
        return colorPrimaryDarkSet(context.colorRes(colorPrimaryDarkRes))
    }

    var statusBarColor by intPref(0, OopsPrefsKey.KEY_STATUS_BAR_COLOR)
    fun statusBarColorSet(@ColorInt statusBarColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_STATUS_BAR_COLOR, statusBarColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun statusBarColorResSet(@ColorRes statusBarColorRes: Int): Oops {
        return statusBarColorSet(context.colorRes(statusBarColorRes))
    }

    var statusBarMode by enumValuePref(StatusBarMode.AUTO, OopsPrefsKey.KEY_STATUS_BAR_MODE)
    fun statusBarModeSet(statusBarMode: StatusBarMode): Oops {
        prefsEditor.putString(OopsPrefsKey.KEY_STATUS_BAR_MODE, statusBarMode.name)
        if (!transaction) prefsEditor.apply()
        return this
    }

    var swipeRefreshLayoutBackgroundColor by intPref(Color.WHITE, OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_BACKGROUND_COLOR)
    fun swipeRefreshLayoutBackgroundColorSet(@ColorInt swipeRefreshLayoutBackgroundColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_BACKGROUND_COLOR, swipeRefreshLayoutBackgroundColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun swipeRefreshLayoutBackgroundColorResSet(@ColorRes swipeRefreshLayoutBackgroundColorRes: Int): Oops {
        return swipeRefreshLayoutBackgroundColorSet(context.colorRes(swipeRefreshLayoutBackgroundColorRes))
    }

    var swipeRefreshLayoutSchemeColor by intArrayPref(intArrayOf(0x3cba54, 0xf4c20d, 0xdb3236, 0x4885ed), OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS)
    fun swipeRefreshLayoutSchemeColorSet(swipeRefreshLayoutSchemeColor: IntArray): Oops {
        if (swipeRefreshLayoutSchemeColor.isEmpty()) throw IllegalArgumentException("color res args size = 0")
        prefsEditor.putString(OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS, swipeRefreshLayoutSchemeColor.joinToString(","))
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

    var navBarColor by intPref(0, OopsPrefsKey.KEY_NAV_BAR_COLOR)
    fun navBarColorSet(@ColorInt navBarColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_NAV_BAR_COLOR, navBarColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun navBarColorResSet(@ColorRes navBarColorRes: Int): Oops {
        return navBarColorSet(context.colorRes(navBarColorRes))
    }

    var windowBackground by intPref(0xFAFAFA, OopsPrefsKey.KEY_WINDOW_BACKGROUND_COLOR)
    fun windowBackgroundSet(@ColorInt windowBackground: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_WINDOW_BACKGROUND_COLOR, windowBackground)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun windowBackgroundResSet(@ColorRes windowBackgroundRes: Int): Oops {
        return windowBackgroundSet(context.colorRes(windowBackgroundRes))
    }

    var textColorPrimary by intPref(0, OopsPrefsKey.KEY_PRIMARY_TEXT_COLOR)
    fun textColorPrimarySet(@ColorInt textColorPrimary: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_PRIMARY_TEXT_COLOR, textColorPrimary)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun textColorPrimaryResSet(@ColorRes textColorPrimaryRes: Int): Oops {
        return textColorPrimarySet(context.colorRes(textColorPrimaryRes))
    }

    var textColorPrimaryInverse by intPref(0, OopsPrefsKey.KEY_PRIMARY_TEXT_INVERSE_COLOR)
    fun textColorPrimaryInverseSet(@ColorInt textColorPrimaryInverse: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_PRIMARY_TEXT_INVERSE_COLOR, textColorPrimaryInverse)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun textColorPrimaryInverseResSet(@ColorRes textColorPrimaryInverseRes: Int): Oops {
        return textColorPrimaryInverseSet(context.colorRes(textColorPrimaryInverseRes))
    }

    var textColorSecondary by intPref(0, OopsPrefsKey.KEY_SECONDARY_TEXT_COLOR)
    fun textColorSecondarySet(@ColorInt textColorSecondary: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SECONDARY_TEXT_COLOR, textColorSecondary)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun textColorSecondaryResSet(@ColorRes textColorSecondaryRes: Int): Oops {
        return textColorSecondarySet(context.colorRes(textColorSecondaryRes))
    }

    var textColorSecondaryInverse by intPref(0, OopsPrefsKey.KEY_SECONDARY_TEXT_INVERSE_COLOR)
    fun textColorSecondaryInverseSet(@ColorInt textColorSecondaryInverse: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SECONDARY_TEXT_INVERSE_COLOR, textColorSecondaryInverse)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun textColorSecondaryInverseResSet(@ColorRes textColorSecondaryInverseRes: Int): Oops {
        return textColorSecondaryInverseSet(context.colorRes(textColorSecondaryInverseRes))
    }

    var toolbarTitleColor by intPref(0, OopsPrefsKey.KEY_TOOLBAR_TITLE_COLOR)
    fun toolbarTitleColorSet(@ColorInt toolbarTitleColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_TOOLBAR_TITLE_COLOR, toolbarTitleColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun toolbarTitleColorResSet(@ColorRes toolbarTitleColorRes: Int): Oops {
        return toolbarTitleColorSet(context.colorRes(toolbarTitleColorRes))
    }

    var toolbarSubtitleColor by intPref(0, OopsPrefsKey.KEY_TOOLBAR_SUBTITLE_COLOR)
    fun toolbarSubtitleColorSet(@ColorInt toolbarSubtitleColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_TOOLBAR_SUBTITLE_COLOR, toolbarSubtitleColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun toolbarSubtitleColorResSet(@ColorRes toolbarSubtitleColorRes: Int): Oops {
        return toolbarSubtitleColorSet(context.colorRes(toolbarSubtitleColorRes))
    }

    var toolbarIconColor by intPref(0, OopsPrefsKey.KEY_TOOLBAR_ICON_COLOR)
    fun toolbarIconColorSet(@ColorInt toolbarIconColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_TOOLBAR_ICON_COLOR, toolbarIconColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun toolbarIconColorResSet(@ColorRes toolbarIconColorRes: Int): Oops {
        return toolbarIconColorSet(context.colorRes(toolbarIconColorRes))
    }

    var snackbarTextColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_TEXT_COLOR)
    fun snackbarTextColorSet(@ColorInt snackBarTextColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SNACK_BAR_TEXT_COLOR, snackBarTextColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun snackbarTextColorResSet(@ColorRes snackBarTextColorRes: Int): Oops {
        return snackbarTextColorSet(context.colorRes(snackBarTextColorRes))
    }

    var snackbarActionColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_ACTION_COLOR)
    fun snackbarActionColorSet(@ColorInt snackBarActionColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SNACK_BAR_ACTION_COLOR, snackBarActionColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun snackbarActionColorResSet(@ColorRes snackBarActionColorRes: Int): Oops {
        return snackbarActionColorSet(context.colorRes(snackBarActionColorRes))
    }

    var snackbarBackgroundColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_BACKGROUND_COLOR)
    fun snackbarBackgroundColorSet(@ColorInt snackBarBackgroundColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SNACK_BAR_BACKGROUND_COLOR, snackBarBackgroundColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun snackbarBackgroundColorResSet(@ColorRes snackBarBackgroundColorRes: Int): Oops {
        return snackbarBackgroundColorSet(context.colorRes(snackBarBackgroundColorRes))
    }

    var navViewSelectedColor by intPref(0, OopsPrefsKey.KEY_NAV_VIEW_SELECTED_COLOR)
    fun navViewSelectedColorSet(@ColorInt navViewSelectedColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_NAV_VIEW_SELECTED_COLOR, navViewSelectedColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun navViewSelectedColorResSet(@ColorRes navViewSelectedColorRes: Int): Oops {
        return navViewSelectedColorSet(context.colorRes(navViewSelectedColorRes))
    }

    var tabLayoutTextColor by intPref(0, OopsPrefsKey.KEY_TAB_LAYOUT_TEXT_COLOR)
    fun tabLayoutTextColorSet(@ColorInt tabLayoutTextColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_TAB_LAYOUT_TEXT_COLOR, tabLayoutTextColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun tabLayoutTextColorResSet(@ColorRes tabLayoutTextColorRes: Int): Oops {
        return tabLayoutTextColorSet(context.colorRes(tabLayoutTextColorRes))
    }

    var tabLayoutSelectedTextColor by intPref(0, OopsPrefsKey.KEY_TAB_LAYOUT_SELECTED_TEXT_COLOR)
    fun tabLayoutSelectedTextColorSet(@ColorInt tabLayoutSelectedTextColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_TAB_LAYOUT_SELECTED_TEXT_COLOR, tabLayoutSelectedTextColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun tabLayoutSelectedTextColorResSet(@ColorRes tabLayoutSelectedTextColorRes: Int): Oops {
        return tabLayoutSelectedTextColorSet(context.colorRes(tabLayoutSelectedTextColorRes))
    }

    var bottomNavigationViewNormalColor by intPref(0, OopsPrefsKey.KEY_BOTTOM_NAV_NORMAL_COLOR)
    fun bottomNavigationViewNormalColorSet(@ColorInt bottomNavigationViewNormalColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_BOTTOM_NAV_NORMAL_COLOR, bottomNavigationViewNormalColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun bottomNavigationViewNormalColorResSet(@ColorRes bottomNavigationViewNormalColorRes: Int): Oops {
        return bottomNavigationViewNormalColorSet(context.colorRes(bottomNavigationViewNormalColorRes))
    }

    var bottomNavigationViewSelectedColor by intPref(0, OopsPrefsKey.KEY_BOTTOM_NAV_SELECTED_COLOR)
    fun bottomNavigationViewSelectedColorSet(@ColorInt bottomNavigationViewSelectedColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_BOTTOM_NAV_SELECTED_COLOR, bottomNavigationViewSelectedColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun bottomNavigationViewSelectedColorResSet(@ColorRes bottomNavigationViewSelectedColorRes: Int): Oops {
        return bottomNavigationViewSelectedColorSet(context.colorRes(bottomNavigationViewSelectedColorRes))
    }

    fun collapsingToolbarDominantColorGet(@NonNull tag: String): Int {
        return prefs.getInt(tag.oopsSignedCollapsingToolbarDominantColorKey(), 0)
    }

    fun getCollapsingToolbarDominantColor(@NonNull tag: String): Int {
        return collapsingToolbarDominantColorGet(tag)
    }

    fun collapsingToolbarDominantColorSet(@NonNull tag: String, @ColorInt collapsingToolbarDominantColor: Int): Oops {
        prefsEditor.putInt(tag.oopsSignedCollapsingToolbarDominantColorKey(), collapsingToolbarDominantColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun setCollapsingToolbarDominantColor(@NonNull tag: String, @ColorInt collapsingToolbarDominantColor: Int) {
        prefsEditor.putInt(tag.oopsSignedCollapsingToolbarDominantColorKey(), collapsingToolbarDominantColor).apply()
    }

    fun collapsingToolbarDominantColorResSet(@NonNull tag: String, @ColorRes collapsingToolbarDominantColorRes: Int): Oops {
        prefsEditor.putInt(tag.oopsSignedCollapsingToolbarDominantColorKey(), context.colorRes(collapsingToolbarDominantColorRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun setCollapsingToolbarDominantColorRes(@NonNull tag: String, @ColorRes collapsingToolbarDominantColorRes: Int) {
        prefsEditor.putInt(tag.oopsSignedCollapsingToolbarDominantColorKey(), context.colorRes(collapsingToolbarDominantColorRes)).apply()
    }

    fun attrColorSet(context: Context, @AttrRes attrId: Int, @ColorInt color: Int): Oops {
        prefsEditor.putInt(context.attrValue(attrId).oopsSignedAttrValue(), color)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun setAttrColor(context: Context, @AttrRes attrId: Int, @ColorInt color: Int) {
        prefsEditor.putInt(context.attrValue(attrId).oopsSignedAttrValue(), color).apply()
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
        return prefs.getInt(attrValue.oopsSignedAttrValue(), 0)
    }

    fun getAttrColor(attrValue: String): Int {
        return attrColorGet(attrValue)
    }

    fun getAttrColor(context: Context, @AttrRes attrId: Int): Int {
        return attrColorGet(context.attrValue(attrId))
    }

    inline fun <reified T : AppCompatActivity> addStaticStatusBarColor(@ColorInt color: Int): Oops {
        return addStaticStatusBarColor(T::class.java, color)
    }

    fun addStaticStatusBarColor(activity: AppCompatActivity, @ColorInt color: Int): Oops {
        return addStaticStatusBarColor(activity::class.java, color)
    }

    fun addStaticStatusBarColor(clazz: Class<out AppCompatActivity>, @ColorInt color: Int): Oops {
        oops.prefsEditor.putInt(clazz.canonicalName.toString().oopsSignedStatusBarColorKey(), color)
        if (!transaction) prefsEditor.apply()
        return this
    }

    inline fun <reified T : AppCompatActivity> addStaticStatusBarColorRes(@ColorRes colorRes: Int): Oops {
        return addStaticStatusBarColorRes(T::class.java, colorRes)
    }

    fun addStaticStatusBarColorRes(activity: AppCompatActivity, @ColorRes colorRes: Int): Oops {
        return addStaticStatusBarColorRes(activity::class.java, colorRes)
    }

    fun addStaticStatusBarColorRes(clazz: Class<out AppCompatActivity>, @ColorRes colorRes: Int): Oops {
        return addStaticStatusBarColor(clazz, context.colorRes(colorRes))
    }

    inline fun <reified T : AppCompatActivity> removeStaticStatusBarColor() {
        removeStaticStatusBarColor(T::class.java)
    }

    fun removeStaticStatusBarColor(activity: AppCompatActivity) {
        removeStaticStatusBarColor(activity::class.java)
    }

    fun removeStaticStatusBarColor(clazz: Class<out AppCompatActivity>) {
        oops.prefsEditor.remove(clazz.canonicalName.toString().oopsSignedStatusBarColorKey())
        if (!transaction) prefsEditor.apply()
    }

    inline fun <reified T : AppCompatActivity> addStaticNavBarColorRes(@ColorRes colorRes: Int): Oops {
        return addStaticNavBarColorRes(T::class.java, colorRes)
    }

    fun addStaticNavBarColorRes(activity: AppCompatActivity, @ColorRes colorRes: Int): Oops {
        return addStaticNavBarColorRes(activity::class.java, colorRes)
    }

    fun addStaticNavBarColorRes(clazz: Class<out AppCompatActivity>, @ColorRes colorRes: Int): Oops {
        return addStaticNavBarColor(clazz, context.colorRes(colorRes))
    }

    inline fun <reified T : AppCompatActivity> addStaticNavBarColor(@ColorInt color: Int): Oops {
        return addStaticNavBarColor(T::class.java, color)
    }

    fun addStaticNavBarColor(activity: AppCompatActivity, @ColorInt color: Int): Oops {
        return addStaticNavBarColor(activity::class.java, color)
    }

    fun addStaticNavBarColor(clazz: Class<out AppCompatActivity>, @ColorInt color: Int): Oops {
        prefsEditor.putInt(clazz.canonicalName.toString().oopsSignedNavBarColorKey(), color)
        if (!transaction) prefsEditor.apply()
        return this
    }

    inline fun <reified T : AppCompatActivity> removeStaticNavBarColor() {
        removeStaticNavBarColor(T::class.java)
    }

    fun removeStaticNavBarColor(activity: AppCompatActivity) {
        removeStaticNavBarColor(activity::class.java)
    }

    fun removeStaticNavBarColor(clazz: Class<out AppCompatActivity>) {
        prefsEditor.remove(clazz.canonicalName.toString().oopsSignedNavBarColorKey())
        if (!transaction) prefsEditor.apply()
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        }
        rippleView = null
    }

    internal fun cancelRippleAnimation() {
        rippleAnimation?.cancel()
        rippleAnimation = null

    }

    internal var transaction = false
    internal var transactionStartTime: Long = 0

    internal val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(OopsPrefsKey.PREFS_NAME, Context.MODE_PRIVATE)
    }

    internal val prefsEditor: SharedPreferences.Editor by lazy {
        prefs.edit()
    }

    private fun intPref(value: Int = 0, key: String): ReadWriteProperty<Oops, Int> = IntPref(value, key)
    private fun intArrayPref(value: IntArray, key: String): ReadWriteProperty<Oops, IntArray> = IntArrayPref(value, key)
    private fun booleanPref(value: Boolean = false, key: String): ReadWriteProperty<Oops, Boolean> = BooleanPref(value, key)
    private inline fun <reified T : Enum<*>> enumValuePref(default: T, key: String = ""): ReadWriteProperty<Oops, T> = EnumValuePref(T::class, default, key)


    private fun begin(): Oops {
        transaction = true
        transactionStartTime = SystemClock.uptimeMillis()
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

    companion object Sub {

        private lateinit var oops: Oops

        internal fun init(context: Context) {
            oops = Oops(context)
        }

        @JvmStatic
        fun immed() = oops.cancel()

        @JvmStatic
        fun bulk() = oops.begin()

        @JvmStatic
        fun bulk(block: Oops.() -> Unit) {
            try {
                oops.begin()
                oops.block()
                oops.apply()
            } catch (e: Exception) {
                oops.cancel()
                loge(e) { "oops config not be save..." }
                throw e
            }
        }

        @JvmStatic
        fun living(activity: AppCompatActivity) = ViewModelProviders.of(activity).get(OopsViewModel::class.java)

        @JvmStatic
        fun living(fragment: Fragment) = ViewModelProviders.of(fragment).get(OopsViewModel::class.java)

        @JvmStatic
        @JvmOverloads
        fun attach(activity: AppCompatActivity, factory: LayoutInflaterFactory? = null) {
            LayoutInflaterCompat.setFactory2(activity.layoutInflater, LayoutInflaterFactory2Impl(activity, factory))
            val theme = oops.theme
            if (theme != 0) {
                activity.setTheme(theme)
            }
            activity.attachOops(theme)
        }

        @JvmStatic
        fun setDefaultLayoutInflaterFactory(layoutInflaterFactory: LayoutInflaterFactory?) {
            oops.layoutInflaterFactory = layoutInflaterFactory
        }

        @JvmStatic
        fun getDefaultLayoutInflaterFactory() = oops.layoutInflaterFactory
    }
}
