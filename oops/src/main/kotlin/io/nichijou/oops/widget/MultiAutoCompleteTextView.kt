package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.*

@SuppressLint("ViewConstructor")
open class MultiAutoCompleteTextView @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : AppCompatMultiAutoCompleteTextView(context, attrs), OopsLifecycleOwner {
  private val attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, android.R.attr.textColor, android.R.attr.textColorHint))
  private val lifecycleRegistry = LifecycleRegistry(this)
  @SuppressLint("ResourceType")
  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      isDarkColor(live(attrValues[android.R.attr.background], colorAccent)!!)
        .observe(this@MultiAutoCompleteTextView, Observer(::tint))
      live(attrValues[android.R.attr.textColor])?.observe(this@MultiAutoCompleteTextView, Observer(::setTextColor))
      live(attrValues[android.R.attr.textColorHint])
        ?.observe(this@MultiAutoCompleteTextView, Observer(::setHighlightColor))
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
