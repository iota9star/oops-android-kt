package io.nichijou.oops

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.ext.*
import io.nichijou.oops.widget.StatusBarMode

open class OopsActivity : AppCompatActivity(), OopsViewLifeAndLive {

    override fun getOopsViewModel(): OopsViewModel = oopsVM

    private val oopsVM by lazy { ViewModelProviders.of(this).get(OopsViewModel::class.java) }

    private var sbc = Pair(false, 0)
    private var nbc = Pair(false, 0)

    fun setOverStatusBarColor(@ColorInt color: Int) {
        this.sbc = Pair(true, color)
        setStatusBarColorCompat(color)
    }

    fun setOverNavBarColor(@ColorInt color: Int) {
        this.nbc = Pair(true, color)
        setNavBarColorCompat(color)
    }

    override fun howToLive() {
        oopsVM.statusBarStateColor.observe(this, Observer {
            when (it.statusBarMode) {
                StatusBarMode.AUTO -> {
                    val statusBarColor = if (sbc.first) {
                        sbc.second
                    } else {
                        it.statusBarColor
                    }
                    val rootView = getRootView()
                    if (rootView is DrawerLayout) {
                        setStatusBarColorCompat(Color.TRANSPARENT)
                        rootView.setStatusBarBackgroundColor(statusBarColor)
                    } else {
                        setStatusBarColorCompat(statusBarColor)
                    }
                    setLightStatusBarCompat(statusBarColor.isColorLight())
                }
                StatusBarMode.DARK -> setLightStatusBarCompat(false)
                StatusBarMode.LIGHT -> setLightStatusBarCompat(true)
            }
        })
        oopsVM.navBarColor.observe(this, Observer {
            val navBarColor = if (nbc.first) {
                nbc.second
            } else {
                it
            }
            setNavBarColorCompat(navBarColor)
        })
        oopsVM.colorPrimary.observe(this, Observer(this::setTaskDescriptionColor))
//        oopsVM.windowBackground.observe(this, Observer {
//            this.window.setBackgroundDrawable(ColorDrawable(it))
//        })
        oopsVM.theme.observe(this, Observer {
            val themeId = this.resId(android.R.attr.theme)
            logi { "themeId: $themeId" }
            if (themeId != -1 && themeId != it) {
                Oops.oops.rippleAnimation?.cancel()
                Oops.oops.rippleAnimation = null
                recreate()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Oops.binding(this)
        howToLive()
        super.onCreate(savedInstanceState)
    }
}