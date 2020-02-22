package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.ext.*

@SuppressLint("RestrictedApi", "ViewConstructor")
open class ActionMenuItemView @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : ActionMenuItemView(context, attrs), OopsLifecycleOwner {
  private var colorStateList: ColorStateList? = null
  private val lifecycleRegistry = LifecycleRegistry(this)
  private fun updateColor(color: Int) {
    this.setTextColor(color)
    colorStateList = PairColor(color).toEnabledSl()
    this.tintIcon(colorStateList!!)
  }

  override fun setIcon(icon: Drawable?) {
    super.setIcon(icon?.tint(colorStateList))
  }

  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      toolbarIconColor.observe(this@ActionMenuItemView, Observer(::updateColor))
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
