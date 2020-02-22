package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.*
import io.nichijou.utils.adjustAlpha

@SuppressLint("ViewConstructor")
open class MaterialBorderlessButton @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : MaterialButton(context, attrs), OopsLifecycleOwner {
  private val attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, com.google.android.material.R.attr.strokeColor))
  private val lifecycleRegistry = LifecycleRegistry(this)
  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      isDarkColor(live(attrValues[android.R.attr.background], colorAccent)!!)
        .observe(this@MaterialBorderlessButton, Observer {
          val textColorSl = ColorStateList(arrayOf(
            intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)),
            intArrayOf(it.color, it.color.adjustAlpha(.56f)))
          setTextColor(textColorSl)
          icon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            icon?.tint(textColorSl)
          } else {
            if (isEnabled) {
              icon?.tint(it.color)
            } else {
              icon?.tint(it.color.adjustAlpha(.56f))
            }
          }
          background?.let { d ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && d is RippleDrawable) {
              d.setColor(ColorStateList.valueOf(it.color.adjustAlpha(.56f)))
            }
          }
          isEnabled = !isEnabled
          isEnabled = !isEnabled
        })
      live(attrValues[com.google.android.material.R.attr.strokeColor])
        ?.observe(this@MaterialBorderlessButton, Observer {
          strokeColor = ColorStateList.valueOf(it)
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
