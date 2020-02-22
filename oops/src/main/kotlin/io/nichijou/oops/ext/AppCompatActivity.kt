package io.nichijou.oops.ext

import android.app.ActivityManager
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsThemeStore
import io.nichijou.oops.widget.BarMode
import io.nichijou.utils.adjustAlpha
import io.nichijou.utils.isColorLight
import io.nichijou.utils.stripAlpha


inline fun AppCompatActivity.applyOopsThemeStore(crossinline block: OopsThemeStore.() -> Unit) {
  this.lifecycleScope.launchWhenCreated {
    ViewModelProvider(this@applyOopsThemeStore).get(OopsThemeStore::class.java).apply {
      block()
    }
  }
}

fun AppCompatActivity.getContentView(): ViewGroup = findViewById(android.R.id.content)

fun AppCompatActivity.setStatusBarColorCompat(@ColorInt statusColor: Int) {
  window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
  window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
  window.statusBarColor = statusColor
}

fun AppCompatActivity.setLightStatusBarCompat(isLight: Boolean) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    val view = this.window.decorView
    val flags = view.systemUiVisibility
    view.systemUiVisibility = if (isLight) {
      flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
      flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
  }
}

fun AppCompatActivity.setLightNavBarCompat(isLight: Boolean) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val view = this.window.decorView
    val flags = view.systemUiVisibility
    view.systemUiVisibility = if (isLight) {
      flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    } else {
      flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
    }
  }
}

fun AppCompatActivity.setNavBarColorCompat(@ColorInt color: Int) {
  this.window.navigationBarColor = color
}

fun AppCompatActivity.setNavBarDividerColorCompat(@ColorInt color: Int) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    this.window.navigationBarDividerColor = color
  }
}

fun AppCompatActivity.setTaskDescriptionColor(@ColorInt color: Int) {
  val icon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    packageManager.getAppIcon(packageName)
  } else {
    (applicationInfo.loadIcon(packageManager) as BitmapDrawable).bitmap
  }
  if (icon != null) {
    val td = ActivityManager.TaskDescription(title as String, icon, color.stripAlpha())
    setTaskDescription(td)
  }
}

fun AppCompatActivity.layoutNoLimits() {
  this.window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

fun AppCompatActivity.layoutFullScreen() {
  val decorView = this.window.decorView
  decorView.systemUiVisibility = (decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
}

fun AppCompatActivity.translucentStatusBar(hideStatusBarBackground: Boolean = true) {
  window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
  if (hideStatusBarBackground) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.statusBarColor = Color.TRANSPARENT
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
  } else {
    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
  }

  val mContentView = window.findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup
  val mChildView = mContentView.getChildAt(0)
  if (mChildView != null) {
    mChildView.fitsSystemWindows = false
    ViewCompat.requestApplyInsets(mChildView)
  }
}

internal fun AppCompatActivity.attachOops(themeId: Int) {
  applyOopsThemeStore {
    theme.observe(this@attachOops, Observer {
      if (themeId != it) {
        Oops.immed().cancelRippleAnimation()
        this@attachOops.recreate()
      }
    })
    colorPrimary.observe(this@attachOops, Observer(::setTaskDescriptionColor))
    windowBackground.observe(this@attachOops, Observer {
      this@attachOops.window.setBackgroundDrawable(ColorDrawable(it))
    })
    var isLight: Boolean
    statusBarStateColor.observe(this@attachOops, Observer {
      if (Oops.immed().prefs.getBoolean(this::class.java.name.disableStatusBarKey(), false)) return@Observer
      isLight = it.color.isColorLight()
      when (it.mode) {
        BarMode.AUTO -> setLightStatusBarCompat(isLight)
        BarMode.DARK -> setLightStatusBarCompat(false)
        BarMode.LIGHT -> setLightStatusBarCompat(true)
      }
      setStatusBarColorCompat(it.color)
      val rootView = this@attachOops.getContentView().getChildAt(0)
      if (rootView is DrawerLayout) {
        val sbAlpha = Color.alpha(it.color) / 255f
        rootView.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
          override fun onDrawerOpened(drawerView: View) {
            if (it.mode == BarMode.AUTO) {
              setLightStatusBarCompat(false)
            }
          }

          override fun onDrawerClosed(drawerView: View) {
            if (it.mode == BarMode.AUTO) {
              setLightStatusBarCompat(isLight)
            }
          }

          override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            if (sbAlpha > 0f) {
              val factor = sbAlpha - slideOffset * sbAlpha
              setStatusBarColorCompat(it.color.adjustAlpha(factor))
            }
          }
        })
      }
    })

    navBarStateColor.observe(this@attachOops, Observer {
      if (Oops.immed().prefs.getBoolean(this::class.java.name.disableStatusBarKey(), false)) return@Observer
      setNavBarColorCompat(it.color)
      when (it.mode) {
        BarMode.AUTO -> setLightNavBarCompat(it.color.isColorLight())
        BarMode.DARK -> setLightNavBarCompat(false)
        BarMode.LIGHT -> setLightNavBarCompat(true)
      }
    })
    navBarDividerColor.observe(this@attachOops, Observer(::setNavBarDividerColorCompat))
  }

}
