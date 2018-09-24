package io.nichijou.oops.ext

import android.app.ActivityManager
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity


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
