package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.R
import io.nichijou.oops.ext.*

class ExtendedFloatingActionButton @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : ExtendedFloatingActionButton(context, attrs), OopsLifecycleOwner {
  private val attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, R.attr.strokeColor, R.attr.rippleColor, android.R.attr.textColor))
  private val lifecycleRegistry = LifecycleRegistry(this)
  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      materialButtonColor(
        live(attrValues[android.R.attr.textColor]),
        live(attrValues[R.attr.strokeColor]),
        live(attrValues[R.attr.rippleColor]),
        live(attrValues[android.R.attr.background], colorAccent),
        isDark
      ).observe(this@ExtendedFloatingActionButton, Observer(::tint))
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
