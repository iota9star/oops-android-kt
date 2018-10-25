package io.nichijou.oops

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.SystemClock
import android.view.View
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import io.nichijou.oops.ext.*
import io.nichijou.oops.pref.BooleanPref
import io.nichijou.oops.pref.EnumValuePref
import io.nichijou.oops.pref.IntArrayPref
import io.nichijou.oops.pref.IntPref
import io.nichijou.oops.widget.*
import kotlin.properties.ReadWriteProperty


@SuppressLint("CommitPrefEdits")
class Oops private constructor(val context: Context) {
    val isFirstTime: Boolean
        get() {
            val isFirst = prefs.getBoolean(OopsPrefsKey.KEY_IS_FIRST_TIME, true)
            prefsEditor.putBoolean(OopsPrefsKey.KEY_IS_FIRST_TIME, false).apply()
            return isFirst
        }

    var theme by intPref(0, OopsPrefsKey.KEY_THEME)

    fun theme(@StyleRes theme: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_THEME, theme)
        if (!transaction) prefsEditor.apply()
        return this
    }

    var isDark by booleanPref(false, OopsPrefsKey.KEY_IS_DARK)
    fun isDark(isDark: Boolean): Oops {
        prefsEditor.putBoolean(OopsPrefsKey.KEY_IS_DARK, isDark)
        if (!transaction) prefsEditor.apply()
        return this
    }

    var colorAccent by intPref(0, OopsPrefsKey.KEY_COLOR_ACCENT)
    fun colorAccent(@ColorInt colorAccent: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLOR_ACCENT, colorAccent)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun colorAccentRes(@ColorRes colorAccentRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLOR_ACCENT, context.colorRes(colorAccentRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var colorPrimary by intPref(0, OopsPrefsKey.KEY_COLOR_PRIMARY)
    fun colorPrimary(@ColorInt colorPrimary: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLOR_PRIMARY, colorPrimary)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun colorPrimaryRes(@ColorRes colorPrimaryRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLOR_PRIMARY, context.colorRes(colorPrimaryRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var colorPrimaryDark by intPref(0, OopsPrefsKey.KEY_COLOR_PRIMARY_DARK)
    fun colorPrimaryDark(@ColorInt colorPrimaryDark: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLOR_PRIMARY_DARK, colorPrimaryDark)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun colorPrimaryDarkRes(@ColorRes colorPrimaryDarkRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLOR_PRIMARY_DARK, context.colorRes(colorPrimaryDarkRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var statusBarColor by intPref(0, OopsPrefsKey.KEY_STATUS_BAR_COLOR)
    fun statusBarColor(@ColorInt statusBarColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_STATUS_BAR_COLOR, statusBarColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun statusBarColorRes(@ColorRes statusBarColorRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_STATUS_BAR_COLOR, context.colorRes(statusBarColorRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var statusBarMode by enumValuePref(StatusBarMode.AUTO, OopsPrefsKey.KEY_STATUS_BAR_MODE)
    fun statusBarMode(statusBarMode: StatusBarMode): Oops {
        prefsEditor.putString(OopsPrefsKey.KEY_STATUS_BAR_MODE, statusBarMode.name)
        if (!transaction) prefsEditor.apply()
        return this
    }

    var swipeRefreshLayoutBackgroundColor by intPref(Color.WHITE, OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_BACKGROUND_COLOR)
    fun swipeRefreshLayoutBackgroundColor(@ColorInt swipeRefreshLayoutBackgroundColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_BACKGROUND_COLOR, swipeRefreshLayoutBackgroundColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun swipeRefreshLayoutBackgroundColorRes(@ColorRes swipeRefreshLayoutBackgroundColorRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_BACKGROUND_COLOR, context.colorRes(swipeRefreshLayoutBackgroundColorRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var swipeRefreshLayoutSchemeColor by intArrayPref(intArrayOf(0x3cba54, 0xf4c20d, 0xdb3236, 0x4885ed), OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS)
    fun swipeRefreshLayoutSchemeColor(swipeRefreshLayoutSchemeColor: IntArray): Oops {
        if (swipeRefreshLayoutSchemeColor.isEmpty()) throw IllegalArgumentException("color res args size = 0")
        prefsEditor.putString(OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS, swipeRefreshLayoutSchemeColor.joinToString(","))
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun swipeRefreshLayoutSchemeColors(@ColorInt vararg swipeRefreshLayoutSchemeColor: Int): Oops {
        if (swipeRefreshLayoutSchemeColor.isEmpty()) throw IllegalArgumentException("color res args size = 0")
        prefsEditor.putString(OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS, swipeRefreshLayoutSchemeColor.joinToString(","))
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun swipeRefreshLayoutSchemeColorRes(swipeRefreshLayoutSchemeColorRes: IntArray): Oops {
        if (swipeRefreshLayoutSchemeColorRes.isEmpty()) throw IllegalArgumentException("color array size = 0")
        prefsEditor.putString(OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS, swipeRefreshLayoutSchemeColorRes.map { context.colorRes(it) }.toIntArray().joinToString(","))
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun swipeRefreshLayoutSchemeColorsRes(@ColorRes vararg swipeRefreshLayoutSchemeColorRes: Int): Oops {
        if (swipeRefreshLayoutSchemeColorRes.isEmpty()) throw IllegalArgumentException("color res args size = 0")
        prefsEditor.putString(OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS, swipeRefreshLayoutSchemeColorRes.map { context.colorRes(it) }.toIntArray().joinToString(","))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var navBarColor by intPref(0, OopsPrefsKey.KEY_NAV_BAR_COLOR)
    fun navBarColor(@ColorInt navBarColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_NAV_BAR_COLOR, navBarColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun navBarColorRes(@ColorRes navBarColorRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_NAV_BAR_COLOR, context.colorRes(navBarColorRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var windowBackground by intPref(0xFAFAFA, OopsPrefsKey.KEY_WINDOW_BACKGROUND_COLOR)
    fun windowBackground(@ColorInt windowBackground: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_WINDOW_BACKGROUND_COLOR, windowBackground)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun windowBackgroundRes(@ColorRes windowBackgroundRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_WINDOW_BACKGROUND_COLOR, context.colorRes(windowBackgroundRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var textColorPrimary by intPref(0, OopsPrefsKey.KEY_PRIMARY_TEXT_COLOR)
    fun textColorPrimary(@ColorInt textColorPrimary: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_PRIMARY_TEXT_COLOR, textColorPrimary)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun textColorPrimaryRes(@ColorRes textColorPrimaryRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_PRIMARY_TEXT_COLOR, context.colorRes(textColorPrimaryRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var textColorPrimaryInverse by intPref(0, OopsPrefsKey.KEY_PRIMARY_TEXT_INVERSE_COLOR)
    fun textColorPrimaryInverse(@ColorInt textColorPrimaryInverse: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_PRIMARY_TEXT_INVERSE_COLOR, textColorPrimaryInverse)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun textColorPrimaryInverseRes(@ColorRes textColorPrimaryInverseRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_PRIMARY_TEXT_INVERSE_COLOR, context.colorRes(textColorPrimaryInverseRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var textColorSecondary by intPref(0, OopsPrefsKey.KEY_SECONDARY_TEXT_COLOR)
    fun textColorSecondary(@ColorInt textColorSecondary: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SECONDARY_TEXT_COLOR, textColorSecondary)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun textColorSecondaryRes(@ColorRes textColorSecondaryRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SECONDARY_TEXT_COLOR, context.colorRes(textColorSecondaryRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var textColorSecondaryInverse by intPref(0, OopsPrefsKey.KEY_SECONDARY_TEXT_INVERSE_COLOR)
    fun textColorSecondaryInverse(@ColorInt textColorSecondaryInverse: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SECONDARY_TEXT_INVERSE_COLOR, textColorSecondaryInverse)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun textColorSecondaryInverseRes(@ColorRes textColorSecondaryInverseRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SECONDARY_TEXT_INVERSE_COLOR, context.colorRes(textColorSecondaryInverseRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var toolbarActiveColor by intPref(0, OopsPrefsKey.KEY_TOOLBAR_TEXT_ICON_ACTIVE_COLOR)
    fun toolbarActiveColor(@ColorInt toolbarActiveColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_TOOLBAR_TEXT_ICON_ACTIVE_COLOR, toolbarActiveColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun toolbarActiveColorRes(@ColorRes toolbarActiveColorRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_TOOLBAR_TEXT_ICON_ACTIVE_COLOR, context.colorRes(toolbarActiveColorRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var toolbarInactiveColor by intPref(0, OopsPrefsKey.KEY_TOOLBAR_TEXT_ICON_INACTIVE_COLOR)
    fun toolbarInactiveColor(@ColorInt toolbarInactiveColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_TOOLBAR_TEXT_ICON_INACTIVE_COLOR, toolbarInactiveColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun toolbarInactiveColorRes(@ColorRes toolbarInactiveColorRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_TOOLBAR_TEXT_ICON_INACTIVE_COLOR, context.colorRes(toolbarInactiveColorRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var snackBarTextColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_TEXT_COLOR)
    fun snackBarTextColor(@ColorInt snackBarTextColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SNACK_BAR_TEXT_COLOR, snackBarTextColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun snackBarTextColorRes(@ColorRes snackBarTextColorRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SNACK_BAR_TEXT_COLOR, context.colorRes(snackBarTextColorRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var snackBarActionColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_ACTION_COLOR)
    fun snackBarActionColor(@ColorInt snackBarActionColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SNACK_BAR_ACTION_COLOR, snackBarActionColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun snackBarActionColorRes(@ColorRes snackBarActionColorRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SNACK_BAR_ACTION_COLOR, context.colorRes(snackBarActionColorRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var snackBarBackgroundColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_BACKGROUND_COLOR)
    fun snackBarBackgroundColor(@ColorInt snackBarBackgroundColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SNACK_BAR_BACKGROUND_COLOR, snackBarBackgroundColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun snackBarBackgroundColorRes(@ColorRes snackBarBackgroundColorRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_SNACK_BAR_BACKGROUND_COLOR, context.colorRes(snackBarBackgroundColorRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    var navigationViewMode by enumValuePref(NavigationViewTintMode.PRIMARY, OopsPrefsKey.KEY_NAV_VIEW_MODE)
    fun navigationViewMode(navigationViewMode: NavigationViewTintMode): Oops {
        prefsEditor.putString(OopsPrefsKey.KEY_NAV_VIEW_MODE, navigationViewMode.name)
        if (!transaction) prefsEditor.apply()
        return this
    }

    var tabLayoutBackgroundMode by enumValuePref(TabLayoutBackgroundMode.PRIMARY, OopsPrefsKey.KEY_TAB_LAYOUT_BACKGROUND_MODE)
    fun tabLayoutBackgroundMode(tabLayoutBackgroundMode: TabLayoutBackgroundMode): Oops {
        prefsEditor.putString(OopsPrefsKey.KEY_TAB_LAYOUT_BACKGROUND_MODE, tabLayoutBackgroundMode.name)
        if (!transaction) prefsEditor.apply()
        return this
    }

    var tabLayoutIndicatorMode by enumValuePref(TabLayoutIndicatorMode.ACCENT, OopsPrefsKey.KEY_TAB_LAYOUT_INDICATOR_MODE)
    fun tabLayoutIndicatorMode(tabLayoutIndicatorMode: TabLayoutIndicatorMode): Oops {
        prefsEditor.putString(OopsPrefsKey.KEY_TAB_LAYOUT_INDICATOR_MODE, tabLayoutIndicatorMode.name)
        if (!transaction) prefsEditor.apply()
        return this
    }

    var bottomNavigationViewBackgroundMode by enumValuePref(BottomNavigationViewBackgroundMode.AUTO, OopsPrefsKey.KEY_BOTTOM_NAV_BACKGROUND_MODE)
    fun bottomNavigationViewBackgroundMode(bottomNavigationViewBackgroundMode: BottomNavigationViewBackgroundMode): Oops {
        prefsEditor.putString(OopsPrefsKey.KEY_BOTTOM_NAV_BACKGROUND_MODE, bottomNavigationViewBackgroundMode.name)
        if (!transaction) prefsEditor.apply()
        return this
    }

    var bottomNavigationViewIconTextMode by enumValuePref(BottomNavigationViewIconTextMode.AUTO, OopsPrefsKey.KEY_BOTTOM_NAV_ICON_TEXT_MODE)
    fun bottomNavigationViewIconTextMode(bottomNavigationViewIconTextMode: BottomNavigationViewIconTextMode): Oops {
        prefsEditor.putString(OopsPrefsKey.KEY_BOTTOM_NAV_ICON_TEXT_MODE, bottomNavigationViewIconTextMode.name)
        if (!transaction) prefsEditor.apply()
        return this
    }

    var collapsingToolbarDominantColor by intPref(0, OopsPrefsKey.KEY_COLLAPSING_TOOLBAR_DOMINANT_COLOR)
    fun collapsingToolbarDominantColor(@ColorInt collapsingToolbarDominantColor: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLLAPSING_TOOLBAR_DOMINANT_COLOR, collapsingToolbarDominantColor)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun collapsingToolbarDominantColorRes(@ColorRes collapsingToolbarDominantColorRes: Int): Oops {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLLAPSING_TOOLBAR_DOMINANT_COLOR, context.colorRes(collapsingToolbarDominantColorRes))
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun customAttrColor(context: Context, @AttrRes attrId: Int, color: Int): Oops {
        prefsEditor.putInt(context.attrName(attrId).oopsSignedAttrName(), color)
        if (!transaction) prefsEditor.apply()
        return this
    }

    fun customAttrColor(context: Context, @AttrRes attrId: Int): Int {
        return prefs.getInt(context.attrName(attrId).oopsSignedAttrName(), 0)
    }

    internal fun customAttrColor(attrName: String): Int {
        return prefs.getInt(attrName.oopsSignedAttrName(), 0)
    }

    inline fun <reified T : AppCompatActivity> putStaticStatusBarColorRes(@ColorRes colorRes: Int) {
        putStaticStatusBarColorRes(T::class.java, colorRes)
    }

    fun putStaticStatusBarColorRes(activity: AppCompatActivity, @ColorRes colorRes: Int) {
        putStaticStatusBarColorRes(activity::class.java, colorRes)
    }

    fun putStaticStatusBarColorRes(clazz: Class<out AppCompatActivity>, @ColorRes colorRes: Int) {
        putStaticNavBarColor(clazz, context.colorRes(colorRes))
    }

    inline fun <reified T : AppCompatActivity> putStaticStatusBarColor(@ColorInt color: Int) {
        putStaticStatusBarColor(T::class.java, color)
    }

    fun putStaticStatusBarColor(activity: AppCompatActivity, @ColorInt color: Int) {
        putStaticStatusBarColor(activity::class.java, color)
    }

    fun putStaticStatusBarColor(clazz: Class<out AppCompatActivity>, @ColorInt color: Int) {
        oops.prefsEditor.putInt(clazz.canonicalName.toString().oopsSignedStatusBarColorKey(), color)
        if (!transaction) prefsEditor.apply()
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

    inline fun <reified T : AppCompatActivity> putStaticNavBarColorRes(@ColorRes colorRes: Int) {
        putStaticNavBarColorRes(T::class.java, colorRes)
    }

    fun putStaticNavBarColorRes(activity: AppCompatActivity, @ColorRes colorRes: Int) {
        putStaticNavBarColorRes(activity::class.java, colorRes)
    }

    fun putStaticNavBarColorRes(clazz: Class<out AppCompatActivity>, @ColorRes colorRes: Int) {
        putStaticNavBarColor(clazz, context.colorRes(colorRes))
    }

    inline fun <reified T : AppCompatActivity> putStaticNavBarColor(@ColorInt color: Int) {
        putStaticNavBarColor(T::class.java, color)
    }

    fun putStaticNavBarColor(activity: AppCompatActivity, @ColorInt color: Int) {
        putStaticNavBarColor(activity::class.java, color)
    }

    fun putStaticNavBarColor(clazz: Class<out AppCompatActivity>, @ColorInt color: Int) {
        prefsEditor.putInt(clazz.canonicalName.toString().oopsSignedNavBarColorKey(), color)
        if (!transaction) prefsEditor.apply()
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    var rippleView: View? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @IntRange(from = 300, to = 1200)
    var rippleAnimDuration: Long = 480

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

    private fun cancel() {
        transaction = false
    }

    companion object {

        private lateinit var oops: Oops

        internal fun init(context: Context) {
            oops = Oops(context)
        }

        @JvmStatic
        fun once() = oops

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
        fun attach(activity: AppCompatActivity) {
            attach(activity, null)
        }

        @JvmStatic
        fun attach(activity: AppCompatActivity, factory: OopsLayoutInflaterFactory?) {
            LayoutInflaterCompat.setFactory2(activity.layoutInflater, OopsFactory2Impl(activity, factory))
            val theme = oops.theme
            if (theme != 0) {
                activity.setTheme(theme)
            }
            activity.attachOops(theme)
        }
    }
}
