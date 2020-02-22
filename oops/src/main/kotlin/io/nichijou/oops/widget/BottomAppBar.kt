package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.bottomappbar.BottomAppBar
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.R
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.ext.*

@SuppressLint("ViewConstructor")
open class BottomAppBar @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : BottomAppBar(context, attrs), OopsLifecycleOwner {

  private val attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, R.attr.backgroundTint, R.attr.titleTextColor, R.attr.subtitleTextColor))

  private var colorStateList: ColorStateList? = null
  private val lifecycleRegistry = LifecycleRegistry(this)

  override fun setNavigationIcon(icon: Drawable?) {
    super.setNavigationIcon(icon?.tint(colorStateList))
  }

  private fun updateColor(color: Int) {
    val active = PairColor(color)
    colorStateList = active.toEnabledSl().also {
      this.tintCollapseIcon(it)
      this.tintNavIcon(it)
    }
    this.tintOverflowIcon(color)
    this.tintMenuItem(active)
  }

  override fun liveInOops() {
    val backgroundTint = attrValues[R.attr.backgroundTint]
    val bgAttrValue = if (backgroundTint.isNullOrBlank()) {
      attrValues[android.R.attr.background]
    } else {
      backgroundTint
    }
    this.activity().applyOopsThemeStore {
      live(bgAttrValue, colorPrimary)!!.observe(this@BottomAppBar, Observer {
        val bg = background
        if (bg != null) {
          background = bg.tint(it)
        } else {
          setBackgroundColor(it)
        }
      })
      live(attrValues[androidx.appcompat.R.attr.titleTextColor], toolbarTitleColor)!!.observe(this@BottomAppBar, Observer(::setTitleTextColor))
      live(attrValues[androidx.appcompat.R.attr.subtitleTextColor], toolbarSubtitleColor)!!.observe(this@BottomAppBar, Observer(::setSubtitleTextColor))
      toolbarIconColor.observe(this@BottomAppBar, Observer(::updateColor))
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
