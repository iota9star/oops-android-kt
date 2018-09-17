package io.nichijou.oops

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.ext.*
import io.nichijou.oops.widget.StatusBarMode

open class OopsActivity : AppCompatActivity(), OopsLifeAndLive {

    private val ovm by lazy { ViewModelProviders.of(this).get(OopsViewModel::class.java) }

    private val currentTheme = Oops.oops.theme

    override fun bindingLive() {
        ovm.statusBarStateColor.observe(this, Observer {
            when (it.statusBarMode) {
                StatusBarMode.AUTO -> {
                    val statusBarColor = it.statusBarColor
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
        ovm.navBarColor.observe(this, Observer(this::setNavBarColorCompat))
        ovm.colorPrimary.observe(this, Observer(this::setTaskDescriptionColor))
        ovm.windowBackground.observe(this, Observer {
            this.window.setBackgroundDrawable(ColorDrawable(it))
        })
        ovm.theme.observe(this, Observer {
            if (currentTheme != it) {
                Oops.oops.rippleAnimation?.cancel()
                recreate()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Oops.binding(this)
        super.onCreate(savedInstanceState)
        bindingLive()
    }
}