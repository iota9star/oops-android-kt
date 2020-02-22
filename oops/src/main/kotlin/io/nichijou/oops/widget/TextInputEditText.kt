package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.color.IsDarkWithColor
import io.nichijou.oops.ext.*

@SuppressLint("ViewConstructor")
open class TextInputEditText @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : TextInputEditText(context, attrs), OopsLifecycleOwner {
  private val backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)
  private var lastState: IsDarkWithColor? = null
  private val lifecycleRegistry = LifecycleRegistry(this)
  private fun updateColor(color: IsDarkWithColor) {
    this.lastState = color
    this.tint(color)
  }

  override fun refreshDrawableState() {
    super.refreshDrawableState()
    lastState?.let {
      post {
        updateColor(lastState!!)
      }
    }
  }

  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      isDarkColor(live(backgroundAttrValue, colorAccent)!!)
        .observe(this@TextInputEditText, Observer(::updateColor))
      textColorPrimary.observe(this@TextInputEditText, Observer(::setTextColor))
      textColorSecondary.observe(this@TextInputEditText, Observer(::setHintTextColor))
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
