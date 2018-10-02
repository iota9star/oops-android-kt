package io.nichijou.oops

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.SystemClock
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import io.nichijou.oops.ext.colorAttr
import io.nichijou.oops.ext.colorRes
import io.nichijou.oops.ext.loge
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
    var colorAccent by intPref(context.colorAttr(R.attr.colorAccent), OopsPrefsKey.KEY_COLOR_ACCENT)
    fun colorAccentRes(@ColorRes colorAccentRes: Int) {
        this.colorAccent = context.colorRes(colorAccentRes)
    }

    var colorPrimary by intPref(context.colorAttr(R.attr.colorPrimary), OopsPrefsKey.KEY_COLOR_PRIMARY)
    fun colorPrimaryRes(@ColorRes colorPrimaryRes: Int) {
        this.colorPrimary = context.colorRes(colorPrimaryRes)
    }

    var colorPrimaryDark by intPref(context.colorAttr(R.attr.colorPrimaryDark), OopsPrefsKey.KEY_COLOR_PRIMARY_DARK)
    fun colorPrimaryDarkRes(@ColorRes colorPrimaryDarkRes: Int) {
        this.colorPrimaryDark = context.colorRes(colorPrimaryDarkRes)
    }

    var statusBarColor by intPref(context.colorAttr(R.attr.colorPrimaryDark), OopsPrefsKey.KEY_STATUS_BAR_COLOR)
    fun statusBarColorRes(@ColorRes statusBarColorRes: Int) {
        this.statusBarColor = context.colorRes(statusBarColorRes)
    }

    var statusBarMode by enumValuePref(StatusBarMode.AUTO, OopsPrefsKey.KEY_STATUS_BAR_MODE)
    var swipeRefreshLayoutBackgroundColor by intPref(Color.WHITE, OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_BACKGROUND_COLOR)
    fun swipeRefreshLayoutBackgroundColorRes(@ColorRes swipeRefreshLayoutBackgroundColorRes: Int) {
        this.swipeRefreshLayoutBackgroundColor = context.colorRes(swipeRefreshLayoutBackgroundColorRes)
    }

    var swipeRefreshLayoutSchemeColor by intArrayPref(intArrayOf(0x3cba54, 0xf4c20d, 0xdb3236, 0x4885ed), OopsPrefsKey.KEY_SWIPE_REFRESH_LAYOUT_SCHEME_COLORS)
    fun swipeRefreshLayoutSchemeColors(@ColorRes vararg swipeRefreshLayoutSchemeColor: Int) {
        if (swipeRefreshLayoutSchemeColor.isEmpty()) throw IllegalArgumentException("color res args size should > 0")
        this.swipeRefreshLayoutSchemeColor = swipeRefreshLayoutSchemeColor
    }

    fun swipeRefreshLayoutSchemeColorRes(swipeRefreshLayoutSchemeColorRes: IntArray) {
        if (swipeRefreshLayoutSchemeColorRes.isEmpty()) throw IllegalArgumentException("color array size should > 0")
        this.swipeRefreshLayoutSchemeColor = swipeRefreshLayoutSchemeColorRes.map { context.colorRes(it) }.toIntArray()
    }

    fun swipeRefreshLayoutSchemeColorsRes(@ColorRes vararg swipeRefreshLayoutSchemeColorRes: Int) {
        if (swipeRefreshLayoutSchemeColorRes.isEmpty()) throw IllegalArgumentException("color res args size should > 0")
        this.swipeRefreshLayoutSchemeColor = swipeRefreshLayoutSchemeColorRes.map { context.colorRes(it) }.toIntArray()
    }

    var navBarColor by intPref(context.colorAttr(R.attr.colorPrimaryDark), OopsPrefsKey.KEY_NAV_BAR_COLOR)
    fun navBarColorRes(@ColorRes navBarColorRes: Int) {
        this.navBarColor = context.colorRes(navBarColorRes)
    }

    var windowBackground by intPref(context.colorAttr(android.R.attr.windowBackground), OopsPrefsKey.KEY_WINDOW_BACKGROUND_COLOR)
    fun windowBackgroundRes(@ColorRes windowBackgroundRes: Int) {
        this.windowBackground = context.colorRes(windowBackgroundRes)
    }

    var textColorPrimary by intPref(context.colorAttr(android.R.attr.textColorPrimary), OopsPrefsKey.KEY_PRIMARY_TEXT_COLOR)
    fun textColorPrimaryRes(@ColorRes textColorPrimaryRes: Int) {
        this.textColorPrimary = context.colorRes(textColorPrimaryRes)
    }

    var textColorPrimaryInverse by intPref(context.colorAttr(android.R.attr.textColorPrimaryInverse), OopsPrefsKey.KEY_PRIMARY_TEXT_INVERSE_COLOR)
    fun textColorPrimaryInverseRes(@ColorRes textColorPrimaryInverseRes: Int) {
        this.textColorPrimaryInverse = context.colorRes(textColorPrimaryInverseRes)
    }

    var textColorSecondary by intPref(context.colorAttr(android.R.attr.textColorSecondary), OopsPrefsKey.KEY_SECONDARY_TEXT_COLOR)
    fun textColorSecondaryRes(@ColorRes textColorSecondaryRes: Int) {
        this.textColorSecondary = context.colorRes(textColorSecondaryRes)
    }

    var textColorSecondaryInverse by intPref(context.colorAttr(android.R.attr.textColorSecondaryInverse), OopsPrefsKey.KEY_SECONDARY_TEXT_INVERSE_COLOR)
    fun textColorSecondaryInverseRes(@ColorRes textColorSecondaryInverseRes: Int) {
        this.textColorSecondaryInverse = context.colorRes(textColorSecondaryInverseRes)
    }

    var iconTitleActiveColor by intPref(0, OopsPrefsKey.KEY_ICON_TITLE_ACTIVE_COLOR)
    fun iconTitleActiveColorRes(@ColorRes iconTitleActiveColorRes: Int) {
        this.iconTitleActiveColor = context.colorRes(iconTitleActiveColorRes)
    }

    var iconTitleInactiveColor by intPref(0, OopsPrefsKey.KEY_ICON_TITLE_INACTIVE_COLOR)
    fun iconTitleInactiveColorRes(@ColorRes iconTitleInactiveColorRes: Int) {
        this.iconTitleInactiveColor = context.colorRes(iconTitleInactiveColorRes)
    }

    var snackBarTextColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_TEXT_COLOR)
    fun snackBarTextColorRes(@ColorRes snackBarTextColorRes: Int) {
        this.snackBarTextColor = context.colorRes(snackBarTextColorRes)
    }

    var snackBarActionColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_ACTION_COLOR)
    fun snackBarActionColorRes(@ColorRes snackBarActionColorRes: Int) {
        this.snackBarActionColor = context.colorRes(snackBarActionColorRes)
    }

    var snackBarBackgroundColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_BACKGROUND_COLOR)
    fun snackBarBackgroundColorRes(@ColorRes snackBarBackgroundColorRes: Int) {
        this.snackBarBackgroundColor = context.colorRes(snackBarBackgroundColorRes)
    }

    var cardViewBackgroundColor by intPref(0, OopsPrefsKey.KEY_CARD_VIEW_BACKGROUND_COLOR)
    fun cardViewBackgroundColorRes(@ColorRes cardViewBackgroundColorRes: Int) {
        this.cardViewBackgroundColor = context.colorRes(cardViewBackgroundColorRes)
    }

    var navigationViewMode by enumValuePref(NavigationViewTintMode.PRIMARY, OopsPrefsKey.KEY_NAV_VIEW_MODE)
    var tabLayoutBackgroundMode by enumValuePref(TabLayoutBackgroundMode.PRIMARY, OopsPrefsKey.KEY_TAB_LAYOUT_BACKGROUND_MODE)
    var tabLayoutIndicatorMode by enumValuePref(TabLayoutIndicatorMode.ACCENT, OopsPrefsKey.KEY_TAB_LAYOUT_INDICATOR_MODE)
    var bottomNavigationViewBackgroundMode by enumValuePref(BottomNavigationViewBackgroundMode.AUTO, OopsPrefsKey.KEY_BOTTOM_NAV_BACKGROUND_MODE)
    var bottomNavigationViewIconTextMode by enumValuePref(BottomNavigationViewIconTextMode.AUTO, OopsPrefsKey.KEY_BOTTOM_NAV_ICON_TEXT_MODE)
    var collapsingToolbarColor by intPref(0, OopsPrefsKey.KEY_COLLAPSING_TOOLBAR_COLOR)
    fun collapsingToolbarColorRes(@ColorRes collapsingToolbarColorRes: Int) {
        this.collapsingToolbarColor = context.colorRes(collapsingToolbarColorRes)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    var rippleView: View? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @IntRange(from = 300, to = 1600)
    var rippleAnimDuration: Long = 520

    internal var rippleAnimation: RippleAnimation? = null

    private fun showRippleAnimation() {
        if (rippleView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rippleAnimation = RippleAnimation.create(rippleView!!)
            rippleAnimation!!.setDuration(rippleAnimDuration).start()
            rippleAnimation!!.setOnAnimationEndListener(object : RippleAnimation.OnAnimationEndListener {
                override fun onAnimationEnd() {
                    rippleAnimation = null
                }
            })
        }
        rippleView = null
    }

    internal var transaction = false
    internal var transactionStartTime: Long = 0

    internal val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(OopsPrefsKey.PREFS_NAME, Context.MODE_PRIVATE)
    }

    internal val prefsEditor: SharedPreferences.Editor by lazy {
        prefs.edit()
    }

    private fun intArrayPref(value: IntArray, key: String): ReadWriteProperty<Oops, IntArray> = IntArrayPref(value, key)
    private fun intPref(value: Int = 0, key: String): ReadWriteProperty<Oops, Int> = IntPref(value, key)
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

        internal fun init(context: Context) {
            oops = Oops(context)
        }

        fun binding(activity: AppCompatActivity) {
            LayoutInflaterCompat.setFactory2(activity.layoutInflater, OopsFactory2Impl(activity))
            val theme = oops.theme
            if (theme != 0) {
                activity.setTheme(theme)
            }
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
            oops.showRippleAnimation()
            oops.apply()
        }
    }
}
