package io.nichijou.oops

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.SystemClock
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import io.nichijou.oops.ext.*
import io.nichijou.oops.pref.*
import io.nichijou.oops.temp.StatusBarStateColor
import io.nichijou.oops.widget.*
import kotlin.collections.set
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty0


@SuppressLint("CommitPrefEdits")
class Oops private constructor(val context: Context) {
    var theme by intPref(0, OopsPrefsKey.KEY_THEME)
    var isDark by booleanPref(false, OopsPrefsKey.KEY_IS_DARK)
    var isFirstTime by booleanPref(false, OopsPrefsKey.KEY_IS_FIRST_TIME)
    var colorAccent by intPref(context.colorAttr(R.attr.colorAccent), OopsPrefsKey.KEY_COLOR_ACCENT)
    var colorPrimary by intPref(context.colorAttr(R.attr.colorPrimary), OopsPrefsKey.KEY_COLOR_PRIMARY)
    var colorPrimaryDark by intPref(context.colorAttr(R.attr.colorPrimaryDark), OopsPrefsKey.KEY_COLOR_PRIMARY_DARK)
    var statusBarColor by intPref(context.colorAttr(R.attr.colorPrimaryDark), OopsPrefsKey.KEY_STATUS_BAR_COLOR)
    var statusBarMode by enumValuePref(StatusBarMode.AUTO, OopsPrefsKey.KEY_STATUS_BAR_MODE)
    var swipeRefreshLayoutBackgroundColor by intPref(0, OopsPrefsKey.KEY_CARD_VIEW_BACKGROUND_COLOR)
    var navBarColor by intPref(context.colorAttr(R.attr.colorPrimaryDark), OopsPrefsKey.KEY_NAV_BAR_COLOR)
    var windowBackground by intPref(context.colorAttr(android.R.attr.windowBackground), OopsPrefsKey.KEY_WINDOW_BACKGROUND_COLOR)
    var textColorPrimary by intPref(context.colorAttr(android.R.attr.textColorPrimary), OopsPrefsKey.KEY_PRIMARY_TEXT_COLOR)
    var textColorPrimaryInverse by intPref(context.colorAttr(android.R.attr.textColorPrimaryInverse), OopsPrefsKey.KEY_PRIMARY_TEXT_INVERSE_COLOR)
    var textColorSecondary by intPref(context.colorAttr(android.R.attr.textColorSecondary), OopsPrefsKey.KEY_SECONDARY_TEXT_COLOR)
    var textColorSecondaryInverse by intPref(context.colorAttr(android.R.attr.textColorSecondaryInverse), OopsPrefsKey.KEY_SECONDARY_TEXT_INVERSE_COLOR)
    var iconTitleActiveColor by intPref(0, OopsPrefsKey.KEY_ICON_TITLE_ACTIVE_COLOR)
    var iconTitleInactiveColor by intPref(0, OopsPrefsKey.KEY_ICON_TITLE_INACTIVE_COLOR)
    var snackBarTextColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_TEXT_COLOR)
    var snackBarActionColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_ACTION_COLOR)
    var snackBarBackgroundColor by intPref(0, OopsPrefsKey.KEY_SNACK_BAR_BACKGROUND_COLOR)
    var cardViewBackgroundColor by intPref(0, OopsPrefsKey.KEY_CARD_VIEW_BACKGROUND_COLOR)
    var navigationViewMode by enumValuePref(NavigationViewTintMode.PRIMARY, OopsPrefsKey.KEY_NAV_VIEW_MODE)
    var tabLayoutBackgroundMode by enumValuePref(TabLayoutBackgroundMode.PRIMARY, OopsPrefsKey.KEY_TAB_LAYOUT_BACKGROUND_MODE)
    var tabLayoutIndicatorMode by enumValuePref(TabLayoutIndicatorMode.ACCENT, OopsPrefsKey.KEY_TAB_LAYOUT_INDICATOR_MODE)
    var bottomNavigationViewBackgroundMode by enumValuePref(BottomNavigationViewBackgroundMode.AUTO, OopsPrefsKey.KEY_BOTTOM_NAV_BACKGROUND_MODE)
    var bottomNavigationViewIconTextMode by enumValuePref(BottomNavigationViewIconTextMode.AUTO, OopsPrefsKey.KEY_BOTTOM_NAV_ICON_TEXT_MODE)
    var collapsingToolbarColor by intPref(0, OopsPrefsKey.KEY_COLLAPSING_TOOLBAR_COLOR)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    var rippleView: View? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @IntRange(from = 300, to = 1600)
    var rippleAnimDuration: Long = 560

    private var rippleAnimation: RippleAnimation? = null

    private fun showRippleAnimation() {
        if (rippleView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rippleAnimation = RippleAnimation.create(rippleView!!)
            rippleAnimation!!.setDuration(rippleAnimDuration).start()
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

    private fun stringPref(value: String = "", key: String): ReadWriteProperty<Oops, String> = StringPref(value, key)
    private fun longPref(value: Long = 0L, key: String): ReadWriteProperty<Oops, Long> = LongPref(value, key)
    private fun floatPref(value: Float = 0F, key: String): ReadWriteProperty<Oops, Float> = FloatPref(value, key)
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

        private var liveStatusBar: MediatorLiveData<StatusBarStateColor>? = null
        private var liveNavBar: LiveData<Int>? = null
        private var liveWindowBackground: LiveData<Int>? = null
        private var livePrimary: LiveData<Int>? = null
        private var liveTheme: LiveData<Int>? = null
        private val lastTheme by lazy { HashMap<Int, Int>() }

        internal fun init(context: Context) {
            oops = Oops(context)
        }

        fun attach(activity: AppCompatActivity) {
            LayoutInflaterCompat.setFactory2(activity.layoutInflater, OopsFactory2Impl())
            val theme = oops.theme
            if (theme != 0) {
                activity.setTheme(theme)
            }
            lastTheme[activity.hashCode()] = theme
            addObservers(activity)
        }

        fun resume(activity: AppCompatActivity) {
            val theme = oops.theme
            if (lastTheme[activity.hashCode()] != theme) {
                lastTheme[activity.hashCode()] = theme
                activity.recreate()
            }
        }

        fun pause(activity: AppCompatActivity) {
            if (activity.isFinishing) {
                lastTheme.remove(activity.hashCode())
                removeObservers(activity)
            }
        }

        fun destroy(activity: AppCompatActivity) {
            lastTheme.remove(activity.hashCode())
            removeObservers(activity)
        }

        private fun removeObservers(owner: LifecycleOwner) {
            liveStatusBar?.removeObservers(owner)
            liveNavBar?.removeObservers(owner)
            liveWindowBackground?.removeObservers(owner)
            livePrimary?.removeObservers(owner)
            liveTheme?.removeObservers(owner)
            liveStatusBar = null
            liveNavBar = null
            liveWindowBackground = null
            livePrimary = null
        }

        private fun addObservers(activity: AppCompatActivity) {
            liveStatusBar = Oops.liveMediator(
                    Oops.live(activity, oops::statusBarColor),
                    Oops.live(activity, oops::statusBarMode),
                    StatusBarStateColor.live())
            liveNavBar = Oops.live(activity, oops::navBarColor)
            liveWindowBackground = Oops.live(activity, oops::windowBackground)
            livePrimary = Oops.live(activity, oops::colorPrimary)
            liveTheme = Oops.live(activity, oops::theme)
            liveStatusBar!!.observe(activity, Observer {
                when (it.statusBarMode) {
                    StatusBarMode.AUTO -> {
                        val statusBarColor = it.statusBarColor
                        val rootView = activity.getRootView()
                        if (rootView is DrawerLayout) {
                            activity.setStatusBarColorCompat(Color.TRANSPARENT)
                            rootView.setStatusBarBackgroundColor(statusBarColor)
                        } else {
                            activity.setStatusBarColorCompat(statusBarColor)
                        }
                        activity.setLightStatusBarCompat(statusBarColor.isColorLight())
                    }
                    StatusBarMode.DARK -> activity.setLightStatusBarCompat(false)
                    StatusBarMode.LIGHT -> activity.setLightStatusBarCompat(true)
                }
            })
            liveNavBar!!.observe(activity, Observer(activity::setNavBarColorCompat))
            liveWindowBackground!!.observe(activity, Observer {
                activity.window.setBackgroundDrawable(ColorDrawable(it))
            })
            livePrimary!!.observe(activity, Observer(activity::setTaskDescriptionColor))
            liveTheme!!.observe(activity, Observer {
                val hashCode = activity.hashCode()
                if (it != lastTheme[hashCode]) {
                    oops.rippleAnimation?.cancel()
                    oops.rippleAnimation = null
                    activity.recreate()
                }
            })
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
            oops.apply()
            oops.showRippleAnimation()
        }

        fun <T> live(owner: LifecycleOwner, property: KProperty0<T>): LiveData<T> {
            return OopsLive(owner, this.oops.prefs, property)
        }

        fun liveColor(ctx: AppCompatActivity, @IdRes resId: Int, fallback: LiveData<Int>?): LiveData<Int>? {
            return when (resId) {
                0 -> fallback
                ctx.resId(R.attr.colorAccent, 0) -> live(ctx, this.oops::colorAccent)
                ctx.resId(R.attr.colorPrimary, 0) -> live(ctx, this.oops::colorPrimary)
                ctx.resId(R.attr.colorPrimaryDark, 0) -> live(ctx, this.oops::colorPrimaryDark)
                ctx.resId(android.R.attr.statusBarColor, 0) -> live(ctx, this.oops::statusBarColor)
                ctx.resId(android.R.attr.windowBackground, 0) -> live(ctx, this.oops::windowBackground)
                ctx.resId(android.R.attr.textColorPrimary, 0) -> live(ctx, this.oops::textColorPrimary)
                ctx.resId(android.R.attr.textColorPrimaryInverse, 0) -> live(ctx, this.oops::textColorPrimaryInverse)
                ctx.resId(android.R.attr.textColorSecondary, 0) -> live(ctx, this.oops::textColorSecondary)
                ctx.resId(android.R.attr.textColorSecondaryInverse, 0) -> live(ctx, this.oops::textColorSecondaryInverse)
                else -> fallback
            }
        }
    }
}
