package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.ext.*

open class BottomNavigationView @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : BottomNavigationView(context, attrs), OopsLifecycleOwner {
  private val backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)
  private val lifecycleRegistry = LifecycleRegistry(this)
  private fun updateColor(pairColor: PairColor) {
    val sl = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(pairColor.first, pairColor.second))
    itemIconTintList = sl
    itemTextColor = sl
  }

  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      live(backgroundAttrValue, colorPrimary)!!.observe(this@BottomNavigationView, Observer(::setBackgroundColor))
      bottomNavStateColor.observe(this@BottomNavigationView, Observer(::updateColor))
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
