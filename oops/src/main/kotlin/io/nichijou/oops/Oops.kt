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
    var isDark by booleanPref(false, OopsPrefsKey.KEY_IS_DARK)
    var colorAccent by intPref(0, OopsPrefsKey.KEY_COLOR_ACCENT)
    fun colorAccentRes(@ColorRes colorAccentRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLOR_ACCENT, context.colorRes(colorAccentRes))
        if (!transaction) prefsEditor.apply()
    }

    var colorPrimary by intPref(0, OopsPrefsKey.KEY_COLOR_PRIMARY)
    fun colorPrimaryRes(@ColorRes colorPrimaryRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLOR_PRIMARY, context.colorRes(colorPrimaryRes))
        if (!transaction) prefsEditor.apply()
    }

    var colorPrimaryDark by intPref(0, OopsPrefsKey.KEY_COLOR_PRIMARY_DARK)
    fun colorPrimaryDarkRes(@ColorRes colorPrimaryDarkRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLOR_PRIMARY_DARK, context.colorRes(colorPrimaryDarkRes))
        if (!transaction) prefsEditor.apply()
    }

    var statusBarColor by intPref(0, OopsPrefsKey.KEY_STATUS_BAR_COLOR)
    fun statusBarColorRes(@ColorRes statusBarColorRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_STATUS_BAR_COLOR, context.colorRes(statusBarColorRes))
        if (!transaction) prefsEditor.apply()
    }

    var statusBarMode by enumValuePref(StatusBarMode.AUTO, OopsPrefsKey.KEY_STATUS_BAR_MODE)

    var swipeRefreshLayoutBackgroundColor by intPref(Color.WHITE, OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_BACKGROUND_COLOR)
    fun swipeRefreshLayoutBackgroundColorRes(@ColorRes swipeRefreshLayoutBackgroundColorRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_BACKGROUND_COLOR, context.colorRes(swipeRefreshLayoutBackgroundColorRes))
        if (!transaction) prefsEditor.apply()
    }

    var swipeRefreshLayoutSchemeColor by intArrayPref(intArrayOf(0x3cba54, 0xf4c20d, 0xdb3236, 0x4885ed), OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS)
    fun swipeRefreshLayoutSchemeColors(@ColorInt vararg swipeRefreshLayoutSchemeColor: Int) {
        if (swipeRefreshLayoutSchemeColor.isEmpty()) throw IllegalArgumentException("color res args size = 0")
        prefsEditor.putString(OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS, swipeRefreshLayoutSchemeColor.joinToString(","))
        if (!transaction) prefsEditor.apply()
    }

    fun swipeRefreshLayoutSchemeColorRes(swipeRefreshLayoutSchemeColorRes: IntArray) {
        if (swipeRefreshLayoutSchemeColorRes.isEmpty()) throw IllegalArgumentException("color array size = 0")
        prefsEditor.putString(OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS, swipeRefreshLayoutSchemeColorRes.map { context.colorRes(it) }.toIntArray().joinToString(","))
        if (!transaction) prefsEditor.apply()
    }

    fun swipeRefreshLayoutSchemeColorsRes(@ColorRes vararg swipeRefreshLayoutSchemeColorRes: Int) {
        if (swipeRefreshLayoutSchemeColorRes.isEmpty()) throw IllegalArgumentException("color res args size = 0")
        prefsEditor.putString(OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS, swipeRefreshLayoutSchemeColorRes.map { context.colorRes(it) }.toIntArray().joinToString(","))
        if (!transaction) prefsEditor.apply()
    }

    var navBarColor by intPref(0, OopsPrefsKey.KEY_NAV_BAR_COLOR)
    fun navBarColorRes(@ColorRes navBarColorRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_NAV_BAR_COLOR, context.colorRes(navBarColorRes))
        if (!transaction) prefsEditor.apply()
    }

    var windowBackground by intPref(0, OopsPrefsKey.KEY_WINDOW_BACKGROUND_COLOR)
    fun windowBackgroundRes(@ColorRes windowBackgroundRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_WINDOW_BACKGROUND_COLOR, context.colorRes(windowBackgroundRes))
        if (!transaction) prefsEditor.apply()
    }

    var textColorPrimary by intPref(0, OopsPrefsKey.KEY_PRIMARY_TEXT_COLOR)
    fun textColorPrimaryRes(@ColorRes textColorPrimaryRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_PRIMARY_TEXT_COLOR, context.colorRes(textColorPrimaryRes))
        if (!transaction) prefsEditor.apply()
    }

    var textColorPrimaryInverse by intPref(0, OopsPrefsKey.KEY_PRIMARY_TEXT_INVERSE_COLOR)
    fun textColorPrimaryInverseRes(@ColorRes textColorPrimaryInverseRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_PRIMARY_TEXT_INVERSE_COLOR, context.colorRes(textColorPrimaryInverseRes))
        if (!transaction) prefsEditor.apply()
    }

    var textColorSecondary by intPref(0, OopsPrefsKey.KEY_SECONDARY_TEXT_COLOR)
    fun textColorSecondaryRes(@ColorRes textColorSecondaryRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_SECONDARY_TEXT_COLOR, context.colorRes(textColorSecondaryRes))
        if (!transaction) prefsEditor.apply()
    }

    var textColorSecondaryInverse by intPref(0, OopsPrefsKey.KEY_SECONDARY_TEXT_INVERSE_COLOR)
    fun textColorSecondaryInverseRes(@ColorRes textColorSecondaryInverseRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_SECONDARY_TEXT_INVERSE_COLOR, context.colorRes(textColorSecondaryInverseRes))
        if (!transaction) prefsEditor.apply()
    }

    var toolbarActiveColor by intPref(0, OopsPrefsKey.KEY_TOOLBAR_TEXT_ICON_ACTIVE_COLOR)
    fun toolbarActiveColorRes(@ColorRes toolbarActiveColorRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_TOOLBAR_TEXT_ICON_ACTIVE_COLOR, context.colorRes(toolbarActiveColorRes))
        if (!transaction) prefsEditor.apply()
    }

    var toolbarInactiveColor by intPref(0, OopsPrefsKey.KEY_TOOLBAR_TEXT_ICON_INACTIVE_COLOR)
    fun toolbarInactiveColorRes(@ColorRes toolbarInactiveColorRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_TOOLBAR_TEXT_ICON_INACTIVE_COLOR, context.colorRes(toolbarInactiveColorRes))
        if (!transaction) prefsEditor.apply()
    }

    var snackBarTextColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_TEXT_COLOR)
    fun snackBarTextColorRes(@ColorRes snackBarTextColorRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_SNACK_BAR_TEXT_COLOR, context.colorRes(snackBarTextColorRes))
        if (!transaction) prefsEditor.apply()
    }

    var snackBarActionColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_ACTION_COLOR)
    fun snackBarActionColorRes(@ColorRes snackBarActionColorRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_SNACK_BAR_ACTION_COLOR, context.colorRes(snackBarActionColorRes))
        if (!transaction) prefsEditor.apply()
    }

    var snackBarBackgroundColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_BACKGROUND_COLOR)
    fun snackBarBackgroundColorRes(@ColorRes snackBarBackgroundColorRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_SNACK_BAR_BACKGROUND_COLOR, context.colorRes(snackBarBackgroundColorRes))
        if (!transaction) prefsEditor.apply()
    }

    var navigationViewMode by enumValuePref(NavigationViewTintMode.PRIMARY, OopsPrefsKey.KEY_NAV_VIEW_MODE)
    var tabLayoutBackgroundMode by enumValuePref(TabLayoutBackgroundMode.PRIMARY, OopsPrefsKey.KEY_TAB_LAYOUT_BACKGROUND_MODE)
    var tabLayoutIndicatorMode by enumValuePref(TabLayoutIndicatorMode.ACCENT, OopsPrefsKey.KEY_TAB_LAYOUT_INDICATOR_MODE)
    var bottomNavigationViewBackgroundMode by enumValuePref(BottomNavigationViewBackgroundMode.AUTO, OopsPrefsKey.KEY_BOTTOM_NAV_BACKGROUND_MODE)
    var bottomNavigationViewIconTextMode by enumValuePref(BottomNavigationViewIconTextMode.AUTO, OopsPrefsKey.KEY_BOTTOM_NAV_ICON_TEXT_MODE)
    var collapsingToolbarColor by intPref(0, OopsPrefsKey.KEY_COLLAPSING_TOOLBAR_COLOR)
    fun collapsingToolbarColorRes(@ColorRes collapsingToolbarColorRes: Int) {
        prefsEditor.putInt(OopsPrefsKey.KEY_COLLAPSING_TOOLBAR_COLOR, context.colorRes(collapsingToolbarColorRes))
        if (!transaction) prefsEditor.apply()
    }

    fun customAttrColor(context: Context, @AttrRes attrId: Int, color: Int) {
        prefsEditor.putInt(context.attrName(attrId).oopsSignedAttrName(), color)
        if (!transaction) prefsEditor.apply()
    }

    fun customAttrColor(context: Context, @AttrRes attrId: Int): Int {
        return prefs.getInt(context.attrName(attrId).oopsSignedAttrName(), 0)
    }

    internal fun customAttrColor(attrName: String): Int {
        return prefs.getInt(attrName.oopsSignedAttrName(), 0)
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


    private fun begin() {
        transaction = true
        transactionStartTime = SystemClock.uptimeMillis()
    }

    private fun apply() {
        prefsEditor.apply()
        transaction = false
    }

    private fun cancel() {
        transaction = false
    }

    companion object {

        lateinit var oops: Oops
        private var factory: OopsLayoutInflaterFactory? = null

        internal fun init(context: Context) {
            oops = Oops(context)
        }

        fun attach(activity: AppCompatActivity) {
            attach(activity, factory)
        }

        fun attach(activity: AppCompatActivity, factory: OopsLayoutInflaterFactory?) {
            LayoutInflaterCompat.setFactory2(activity.layoutInflater, OopsFactory2Impl(activity, factory))
            val theme = oops.theme
            if (theme != 0) {
                activity.setTheme(theme)
            }
            activity.attachOops(theme)
        }

        inline fun <reified T : AppCompatActivity> setStaticStatusBarColorRes(@ColorRes colorRes: Int) {
            setStaticStatusBarColorRes(T::class.java, colorRes)
        }

        fun setStaticStatusBarColorRes(activity: AppCompatActivity, @ColorRes colorRes: Int) {
            setStaticStatusBarColorRes(activity::class.java, colorRes)
        }

        fun setStaticStatusBarColorRes(clazz: Class<out AppCompatActivity>, @ColorRes colorRes: Int) {
            oops.prefsEditor.putInt(clazz.canonicalName.toString().oopsSignedStatusBarColorKey(), oops.context.colorRes(colorRes))
        }

        inline fun <reified T : AppCompatActivity> setStaticStatusBarColor(@ColorInt color: Int) {
            setStaticStatusBarColor(T::class.java, color)
        }

        fun setStaticStatusBarColor(activity: AppCompatActivity, @ColorInt color: Int) {
            setStaticStatusBarColor(activity::class.java, color)
        }

        fun setStaticStatusBarColor(clazz: Class<out AppCompatActivity>, @ColorInt color: Int) {
            oops.prefsEditor.putInt(clazz.canonicalName.toString().oopsSignedStatusBarColorKey(), color)
        }

        inline fun <reified T : AppCompatActivity> removeStaticStatusBarColor() {
            removeStaticStatusBarColor(T::class.java)
        }

        fun removeStaticStatusBarColor(activity: AppCompatActivity) {
            removeStaticStatusBarColor(activity::class.java)
        }

        fun removeStaticStatusBarColor(clazz: Class<out AppCompatActivity>) {
            oops.prefsEditor.remove(clazz.canonicalName.toString().oopsSignedStatusBarColorKey())
        }

        inline fun <reified T : AppCompatActivity> setStaticNavBarColorRes(@ColorRes colorRes: Int) {
            setStaticNavBarColorRes(T::class.java, colorRes)
        }

        fun setStaticNavBarColorRes(activity: AppCompatActivity, @ColorRes colorRes: Int) {
            setStaticNavBarColorRes(activity::class.java, colorRes)
        }

        fun setStaticNavBarColorRes(clazz: Class<out AppCompatActivity>, @ColorRes colorRes: Int) {
            oops.prefsEditor.putInt(clazz.canonicalName.toString().oopsSignedNavBarColorKey(), oops.context.colorRes(colorRes))
        }

        inline fun <reified T : AppCompatActivity> setStaticNavBarColor(@ColorInt color: Int) {
            setStaticNavBarColor(T::class.java, color)
        }

        fun setStaticNavBarColor(activity: AppCompatActivity, @ColorInt color: Int) {
            setStaticNavBarColor(activity::class.java, color)
        }

        fun setStaticNavBarColor(clazz: Class<out AppCompatActivity>, @ColorInt color: Int) {
            oops.prefsEditor.putInt(clazz.canonicalName.toString().oopsSignedNavBarColorKey(), color)
        }

        inline fun <reified T : AppCompatActivity> removeStaticNavBarColor() {
            removeStaticNavBarColor(T::class.java)
        }

        fun removeStaticNavBarColor(activity: AppCompatActivity) {
            removeStaticNavBarColor(activity::class.java)
        }

        fun removeStaticNavBarColor(clazz: Class<out AppCompatActivity>) {
            oops.prefsEditor.remove(clazz.canonicalName.toString().oopsSignedNavBarColorKey())
        }

        fun setLayoutInflaterFactory(factory: OopsLayoutInflaterFactory?) {
            this.factory = factory
        }

        fun oops(block: Oops.() -> Unit) {
            oops.begin()
            try {
                oops.block()
            } catch (e: Exception) {
                oops.cancel()
                loge(e) { "oops config not be save..." }
                throw e
            }
            oops.startRippleAnimation()
            oops.apply()
        }
    }
}
