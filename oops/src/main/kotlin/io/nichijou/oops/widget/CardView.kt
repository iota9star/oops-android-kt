package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.*

@SuppressLint("ViewConstructor")
open class CardView @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : CardView(context, attrs), OopsLifecycleOwner {
  private val backgroundAttrValue = context.attrValue(attrs, androidx.cardview.R.attr.cardBackgroundColor)
  private val lifecycleRegistry = LifecycleRegistry(this)
  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      live(backgroundAttrValue)
        ?.observe(this@CardView, Observer(::setCardBackgroundColor))
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
