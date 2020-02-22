package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.*
import io.nichijou.utils.adjustAlpha

@SuppressLint("ViewConstructor")
open class TextInputLayout @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : TextInputLayout(context, attrs), OopsLifecycleOwner {
  private val backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)
  private val lifecycleRegistry = LifecycleRegistry(this)
  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      live(backgroundAttrValue, colorAccent)!!.observe(this@TextInputLayout, Observer(::setAccentColor))
      textColorSecondary.observe(this@TextInputLayout, Observer {
        setHintColor(it.adjustAlpha(0.7f))
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
