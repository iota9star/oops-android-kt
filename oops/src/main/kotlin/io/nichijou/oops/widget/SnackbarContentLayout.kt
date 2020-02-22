package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.color.SnackbarColor
import io.nichijou.oops.ext.*

@SuppressLint("RestrictedApi", "ViewConstructor")
open class SnackbarContentLayout @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : SnackbarContentLayout(context, attrs), OopsLifecycleOwner {
  private val lifecycleRegistry = LifecycleRegistry(this)
  private fun updateColor(color: SnackbarColor) {
    messageView.setTextColor(color.textColor)
    actionView.apply {
      val bg = background
      if (bg != null) {
        background = bg.tint(color.bgColor)
      } else {
        setBackgroundColor(color.bgColor)
      }
      setTextColor(color.actionColor)
    }
    setBackgroundColor(color.bgColor)
    val parent = this.parent
    if (parent is Snackbar.SnackbarLayout) {
      val background = parent.background
      if (background != null) {
        parent.background = background.tint(color.bgColor)
      } else {
        parent.setBackgroundColor(color.bgColor)
      }
    }
  }

  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      snackBarColor.observe(this@SnackbarContentLayout, Observer(::updateColor))
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
