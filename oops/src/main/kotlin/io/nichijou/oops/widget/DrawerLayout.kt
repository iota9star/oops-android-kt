package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.applyOopsThemeStore
import io.nichijou.oops.ext.pauseOopsLife
import io.nichijou.oops.ext.resumeOopsLife

@SuppressLint("ViewConstructor")
open class DrawerLayout @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : DrawerLayout(context, attrs), OopsLifecycleOwner {
  private var iconColor = 0
  private var arrowDrawable: DrawerArrowDrawable? = null
  private val lifecycleRegistry = LifecycleRegistry(this)
  private fun updateColor() {
    if (iconColor == 0) return
    this.arrowDrawable?.let {
      it.color = iconColor
    }
  }

  override fun addDrawerListener(listener: DrawerListener) {
    super.addDrawerListener(listener)
    if (listener is ActionBarDrawerToggle) {
      this.arrowDrawable = listener.drawerArrowDrawable
      updateColor()
    }
  }

  override fun setDrawerListener(listener: DrawerListener) {
    super.setDrawerListener(listener)
    if (listener is ActionBarDrawerToggle) {
      this.arrowDrawable = listener.drawerArrowDrawable
      updateColor()
    }
  }

  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      toolbarIconColor.observe(this@DrawerLayout, Observer {
        iconColor = it
        updateColor()
      })
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
