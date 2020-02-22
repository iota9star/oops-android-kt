package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.card.MaterialCardView
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.R
import io.nichijou.oops.ext.*

@SuppressLint("ViewConstructor")
open class MaterialCardView @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : MaterialCardView(context, attrs), OopsLifecycleOwner {
  private val attrValues = context.attrValues(attrs, intArrayOf(R.attr.cardBackgroundColor, R.attr.strokeColor, R.attr.checkedIconTint, R.attr.rippleColor))
  private val lifecycleRegistry = LifecycleRegistry(this)
  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      live(attrValues[R.attr.cardBackgroundColor])
        ?.observe(this@MaterialCardView, Observer(::setCardBackgroundColor))
      live(attrValues[R.attr.strokeColor])
        ?.observe(this@MaterialCardView, Observer(::setStrokeColor))
      live(attrValues[R.attr.checkedIconTint], colorPrimary)!!.observe(this@MaterialCardView, Observer {
        checkedIconTint = ColorStateList.valueOf(it)
      })
      live(attrValues[R.attr.rippleColor], colorPrimary)!!.observe(this@MaterialCardView, Observer {
        rippleColor = ColorStateList.valueOf(it)
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
