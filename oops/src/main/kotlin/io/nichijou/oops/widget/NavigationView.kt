package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.R
import io.nichijou.oops.color.IsDarkWithColor
import io.nichijou.oops.ext.*
import io.nichijou.utils.adjustAlpha

@SuppressLint("RestrictedApi", "ViewConstructor")
open class NavigationView @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : NavigationView(context, attrs), OopsLifecycleOwner {
  private val itemTextColorValue = context.attrValue(attrs, R.attr.itemTextColor)
  private val lifecycleRegistry = LifecycleRegistry(this)
  private fun updateColor(color: IsDarkWithColor) {
    val baseColor = if (color.isDark) Color.WHITE else Color.BLACK
    val unselectedIconColor = baseColor.adjustAlpha(.54f)
    val unselectedTextColor = baseColor.adjustAlpha(.87f)
    val selectedItemBgColor = context.colorRes(if (color.isDark) R.color.md_navigation_drawer_selected_dark else R.color.md_navigation_drawer_selected_light)
    this.itemTextColor = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(unselectedTextColor, color.color))
    this.itemIconTintList = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(unselectedIconColor, color.color))
    this.itemBackground = StateListDrawable().apply {
      addState(intArrayOf(android.R.attr.state_checked), ColorDrawable(selectedItemBgColor))
    }
  }

  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      isDarkColor(live(itemTextColorValue, navViewSelectedColor)!!)
        .observe(this@NavigationView, Observer(::updateColor))
    }
  }

  override fun getLifecycle(): Lifecycle = lifecycleRegistry
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    attachOopsLife()
  }

  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    if (visibility == View.VISIBLE) {
      super.onVisibilityChanged(changedView, visibility)
      changedView.resumeOopsLife()
    } else {
      changedView.pauseOopsLife()
      super.onVisibilityChanged(changedView, visibility)
    }
  }

  override fun onWindowVisibilityChanged(visibility: Int) {
    if (visibility == View.VISIBLE) {
      super.onWindowVisibilityChanged(visibility)
      resumeOopsLife()
    } else {
      pauseOopsLife()
      super.onWindowVisibilityChanged(visibility)
    }
  }

  override fun onDetachedFromWindow() {
    detachOopsLife()
    super.onDetachedFromWindow()
  }
}
