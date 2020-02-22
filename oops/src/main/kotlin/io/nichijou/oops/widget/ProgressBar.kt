package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.*

@SuppressLint("ViewConstructor")
open class ProgressBar @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : ProgressBar(context, attrs), OopsLifecycleOwner {
  private val progressTint = context.attrValue(attrs, android.R.attr.progressTint)
  private val lifecycleRegistry = LifecycleRegistry(this)
  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      live(progressTint, colorAccent)!!.observe(this@ProgressBar, Observer(::tint))
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
