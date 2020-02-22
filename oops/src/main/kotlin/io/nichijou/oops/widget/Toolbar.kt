package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.R
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.ext.*

@SuppressLint("ViewConstructor")
open class Toolbar @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : Toolbar(context, attrs), OopsLifecycleOwner {

  private val lifecycleRegistry = LifecycleRegistry(this)
  override fun getLifecycle(): Lifecycle = lifecycleRegistry

  private val attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, R.attr.titleTextColor, R.attr.subtitleTextColor))
  private var colorStateList: ColorStateList? = null

  override fun setNavigationIcon(icon: Drawable?) {
    super.setNavigationIcon(icon?.tint(colorStateList))
  }

  override fun setCollapseIcon(icon: Drawable?) {
    super.setCollapseIcon(icon?.tint(colorStateList))
  }

  override fun setOverflowIcon(icon: Drawable?) {
    super.setOverflowIcon(icon?.tint(colorStateList))
  }

  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      live(attrValues[android.R.attr.background], colorPrimary)!!.observe(this@Toolbar, Observer(::setBackgroundColor))
      live(attrValues[R.attr.titleTextColor], toolbarTitleColor)!!.observe(this@Toolbar, Observer(::setTitleTextColor))
      live(attrValues[R.attr.subtitleTextColor], toolbarSubtitleColor)!!.observe(this@Toolbar, Observer(::setSubtitleTextColor))
      toolbarIconColor.observe(this@Toolbar, Observer(::updateColor))
    }

  }

  fun updateColor(color: Int) {
    val active = PairColor(color)
    colorStateList = active.toEnabledSl()
    this.tintCollapseIcon(colorStateList)
    this.tintNavIcon(colorStateList)
    this.tintOverflowIcon(color)
    this.tintMenuItem(active)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    attachOopsLife()
  }

  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    if (visibility == View.VISIBLE) {
      super.onVisibilityChanged(changedView, visibility)
      changedView.resumeOopsLife()
      setVisibility(visibility)
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
