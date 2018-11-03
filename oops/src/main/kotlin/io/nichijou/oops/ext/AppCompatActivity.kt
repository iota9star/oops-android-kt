package io.nichijou.oops.ext

import android.app.ActivityManager
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.widget.StatusBarMode


fun AppCompatActivity.getRootView() = (this.findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup

fun AppCompatActivity.setStatusBarColorCompat(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.window.statusBarColor = color
    }
}

fun AppCompatActivity.setLightStatusBarCompat(isLight: Boolean) {
    val view = this.window.decorView
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = view.systemUiVisibility
        flags = if (isLight) {
            flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        view.systemUiVisibility = flags
    }
}

fun AppCompatActivity.setNavBarColorCompat(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.window.navigationBarColor = color
    }
}

fun AppCompatActivity.setTaskDescriptionColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
        return
    }
    val icon = if (Build.VERSION.SDK_INT >= 26) {
        packageManager.getAppIcon(packageName)
    } else {
        (applicationInfo.loadIcon(packageManager) as BitmapDrawable).bitmap
    }
    if (icon != null) {
        val td = ActivityManager.TaskDescription(title as String, icon, color.stripAlpha())
        setTaskDescription(td)
    }
}

fun AppCompatActivity.insetStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val decorView = this.window.decorView
        decorView.systemUiVisibility = (decorView.systemUiVisibility
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        this.window.statusBarColor = Color.TRANSPARENT
    }
}

fun AppCompatActivity.translucentStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        this.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

internal fun AppCompatActivity.attachOops(themeId: Int) {
    val viewModel = Oops.living(this)
    viewModel.theme.observe(this, Observer {
        if (themeId != it) {
            Oops.immed().cancelRippleAnimation()
            this.recreate()
        }
    })
    viewModel.colorPrimary.observe(this, Observer(this::setTaskDescriptionColor))
    viewModel.windowBackground.observe(this, Observer {
        this.window.setBackgroundDrawable(ColorDrawable(it))
    })
    viewModel.statusBarStateColor.observe(this, Observer {
        when (it.statusBarMode) {
            StatusBarMode.AUTO -> {
                val rootView = this.getRootView()
                val key = this::class.java.canonicalName.toString().oopsSignedStatusBarColorKey()
                val statusBarColor = if (Oops.immed().prefs.contains(key)) Oops.immed().prefs.getInt(key, 0) else it.statusBarColor
                if (rootView is DrawerLayout) {
                    this.setStatusBarColorCompat(Color.TRANSPARENT)
                    rootView.setStatusBarBackgroundColor(statusBarColor)
                } else {
                    this.setStatusBarColorCompat(statusBarColor)
                }
                this.setLightStatusBarCompat(statusBarColor.isColorLight())
            }
            StatusBarMode.DARK -> this.setLightStatusBarCompat(false)
            StatusBarMode.LIGHT -> this.setLightStatusBarCompat(true)
        }
    })
    viewModel.navBarColor.observe(this, Observer {
        val key = this::class.java.canonicalName.toString().oopsSignedNavBarColorKey()
        val navBarColor = if (Oops.immed().prefs.contains(key)) Oops.immed().prefs.getInt(key, 0) else it
        this.setNavBarColorCompat(navBarColor)
    })
}
