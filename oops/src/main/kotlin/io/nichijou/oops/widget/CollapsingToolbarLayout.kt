package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.widget.ActionMenuView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import io.nichijou.oops.KEY_DEFAULT_COLLAPSING_TOOLBAR_TAG
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.color.CollapsingToolbarStateColor
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.ext.*
import io.nichijou.utils.adjustAlpha
import io.nichijou.utils.blendWith
import io.nichijou.utils.isColorLight

@SuppressLint("ViewConstructor")
open class CollapsingToolbarLayout @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : CollapsingToolbarLayout(context, attrs), OopsLifecycleOwner, AppBarLayout.OnOffsetChangedListener {
  private val backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)
  private var appBarLayout: AppBarLayout? = null
  private var toolbar: Toolbar? = null
  private var lastOffset = -1
  private var stateColor: CollapsingToolbarStateColor? = null
  private var needLife = false
  private val lifecycleRegistry = LifecycleRegistry(this)
  override fun onOffsetChanged(appBarLayout: AppBarLayout?, offset: Int) {
    if (lastOffset != Math.abs(offset)) {
      updateColor()
    }
    lastOffset = Math.abs(offset)
  }

  private fun updateColor() {
    stateColor?.apply {
      val tlp = toolbar!!.layoutParams as MarginLayoutParams
      val maxOffset = appBarLayout!!.measuredHeight - tlp.height - tlp.topMargin - tlp.bottomMargin
      val ratio = lastOffset.toFloat() / maxOffset.toFloat()
      val blendedColor = dominantColor.blendWith(bgColor, ratio)
      val expandedTitleColor = if (dominantColor.isColorLight()) Color.BLACK else Color.WHITE
      val blendedTitleColor = expandedTitleColor.blendWith(titleColor, ratio)
      toolbar?.setTitleTextColor(titleColor)
      setCollapsedTitleTextColor(titleColor)
      setExpandedTitleColor(expandedTitleColor)
      tintIcon(PairColor(blendedTitleColor, blendedColor.adjustAlpha(.7f)))
    }
  }

  private fun eachChildren(view: ViewGroup) {
    for (i in 0 until view.childCount) {
      val child = view.getChildAt(i)
      if (child is Toolbar) {
        toolbar = child
        return
      } else if (child is ViewGroup) eachChildren(child)
    }
  }

  private fun tintIcon(color: PairColor) {
    toolbar?.apply {
      val sl = color.toEnabledSl()
      tintNavIcon(sl)
      tintCollapseIcon(sl)
      tintOverflowIcon(color.first)
      val colorFilter = PorterDuffColorFilter(color.first, PorterDuff.Mode.SRC_IN)
      for (i in 0 until this.childCount) {
        val v = getChildAt(i)
        if (v is ActionMenuView) {
          for (j in 0 until v.childCount) {
            val itemView = v.getChildAt(j)
            if (itemView is ActionMenuItemView) {
              itemView.detachOopsLife()
              itemView.setTextColor(color.first)
              val drawablesCount = itemView.compoundDrawables.size
              for (k in 0 until drawablesCount) {
                if (itemView.compoundDrawables[k] != null) {
                  itemView.compoundDrawables[k].colorFilter = colorFilter
                }
              }
              itemView.tintIcon(sl)
            }
          }
        }
      }
      tintMenuItem(color)
    }
  }

  override fun liveInOops() {
    val tag = if (this.tag == null) {
      logd("tag is null, Oops will use default key: default_collapsing_toolbar_tag.")
      KEY_DEFAULT_COLLAPSING_TOOLBAR_TAG
    } else {
      this.tag.toString()
    }
    this.activity().applyOopsThemeStore {
      collapsingToolbarStateColor(tag, live(backgroundAttrValue, colorPrimary)!!)
        .observe(this@CollapsingToolbarLayout, Observer {
          stateColor = it
          setContentScrimColor(it.bgColor)
          setStatusBarScrimColor(it.statusBarColor)
          if (appBarLayout != null && toolbar != null) {
            updateColor()
          }
        })
    }
  }

  override fun getLifecycle(): Lifecycle = lifecycleRegistry
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    val viewParent = this.parent
    if (viewParent is AppBarLayout) {
      appBarLayout = viewParent
    }
    if (appBarLayout != null) {
      eachChildren(this)
      if (toolbar == null) {
        appBarLayout = null
      } else {
        needLife = true
        toolbar!!.apply {
          detachOopsLife()
          setBackgroundColor(Color.TRANSPARENT)
          updateColor(Color.WHITE)
        }
        appBarLayout!!.addOnOffsetChangedListener(this)
      }
    }
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
    if (needLife) {
      appBarLayout!!.removeOnOffsetChangedListener(this)
      appBarLayout = null
      toolbar = null
    }
    super.onDetachedFromWindow()
  }
}
