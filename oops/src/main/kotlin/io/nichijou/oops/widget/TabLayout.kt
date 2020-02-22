package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.R
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.ext.*

@SuppressLint("ViewConstructor")
open class TabLayout @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : TabLayout(context, attrs), OopsLifecycleOwner {
  private val attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, R.attr.tabTextColor, R.attr.tabIndicatorColor, R.attr.tabSelectedTextColor))
  private var iconColor: ColorStateList? = null
  private val lifecycleRegistry = LifecycleRegistry(this)
  override fun liveInOops() {
    this.activity().applyOopsThemeStore {
      live(attrValues[android.R.attr.background], colorPrimary)!!.observe(this@TabLayout, Observer(::setBackgroundColor))
      live(attrValues[R.attr.tabIndicatorColor], colorAccent)!!.observe(this@TabLayout, Observer(::setSelectedTabIndicatorColor))

      tabTextIconColor(
        live(attrValues[R.attr.tabTextColor], tabLayoutTextColor)!!,
        live(attrValues[R.attr.tabSelectedTextColor], tabLayoutSelectedTextColor)!!)
        .observe(this@TabLayout, Observer {
          setTabTextColors(it.first, it.second)
          updateIconColor(it)
        })
    }

  }

  internal fun getIconColor() = iconColor
  private fun updateIconColor(color: PairColor) {
    iconColor = ColorStateList(
      arrayOf(intArrayOf(-android.R.attr.state_selected), intArrayOf(android.R.attr.state_selected)),
      intArrayOf(color.first, color.second)
    )
    for (i in 0 until tabCount) {
      val tab = getTabAt(i)
      if (tab != null && tab.icon != null) {
        tab.icon = tab.icon!!.tint(iconColor)
      }
    }
    val slidingTabIndicator = this.getChildAt(0) as ViewGroup
    var tabView: ViewGroup
    for (i in 0 until slidingTabIndicator.childCount) {
      tabView = slidingTabIndicator.getChildAt(i) as ViewGroup
      (tabView.getChildAt(0) as? TabImageView?)?.apply {
        setTabTextColor(iconColor)
        setImageDrawable(drawable)
      }
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
