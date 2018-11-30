package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.color.CollapsingToolbarStateColor
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.adjustAlpha
import io.nichijou.oops.ext.attrValue
import io.nichijou.oops.ext.blendWith
import io.nichijou.oops.ext.isColorLight
import io.nichijou.oops.ext.tintCollapseIcon
import io.nichijou.oops.ext.tintIcon
import io.nichijou.oops.ext.tintMenuItem
import io.nichijou.oops.ext.tintNavIcon
import io.nichijou.oops.ext.tintOverflowIcon


@SuppressLint("ViewConstructor")
class CollapsingToolbarLayout(context: Context, @Nullable attrs: AttributeSet?, private val enabledLiveNow: Boolean = true) : CollapsingToolbarLayout(context, attrs), OopsLifecycleOwner, AppBarLayout.OnOffsetChangedListener {

    private val backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)

    private var appBarLayout: AppBarLayout? = null
    private var toolbar: io.nichijou.oops.widget.Toolbar? = null
    private var lastOffset = 0
    private var stateColor: CollapsingToolbarStateColor? = null
    private var needLife = false

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, offset: Int) {
        if (lastOffset != Math.abs(offset)) {
            updateColor()
        }
        lastOffset = Math.abs(offset)
    }

    private fun updateColor() {
        stateColor?.apply {
            val tlp = toolbar!!.layoutParams as CollapsingToolbarLayout.LayoutParams
            val maxOffset = appBarLayout!!.measuredHeight - tlp.height - tlp.topMargin - tlp.bottomMargin
            val ratio = lastOffset.toFloat() / maxOffset.toFloat()
            val blendedColor = dominantColor.blendWith(bgColor, ratio)
            val expandedTitleColor = if (dominantColor.isColorLight()) Color.BLACK else Color.WHITE
            val blendedTitleColor = expandedTitleColor.blendWith(textColor, ratio)
            setCollapsedTitleTextColor(textColor)
            setExpandedTitleColor(expandedTitleColor)
            tintMenu(toolbar!!, PairColor(blendedTitleColor, blendedColor.adjustAlpha(.7f)))
        }
    }

    private fun eachChildren(view: ViewGroup) {
        for (i in 0 until view.childCount) {
            val child = view.getChildAt(i)
            if (child is io.nichijou.oops.widget.Toolbar) {
                toolbar = child
                return
            } else if (child is ViewGroup) eachChildren(child)
        }
    }

    private fun tintMenu(toolbar: Toolbar, color: PairColor) {
        toolbar.apply {
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
                            itemView.handleOopsLifeDestroy()
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
            tintMenuItem(this.menu ?: return, color)
        }
    }

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.collapsingToolbarStateColor(this.tag.toString(), living.live(backgroundAttrValue, living.colorPrimary)!!).observe(this, Observer {
            stateColor = it
            this.setContentScrimColor(it.bgColor)
            this.setStatusBarScrimColor(it.statusBarColor)
            if (appBarLayout != null && toolbar != null) {
                updateColor()
            }
        })
    }

    private val lifecycleRegistry = LifecycleRegistry(this)

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
                    handleOopsLifeDestroy()
                    setBackgroundColor(Color.TRANSPARENT)
                    updateColor(Color.WHITE)
                }
                appBarLayout!!.addOnOffsetChangedListener(this)
                if (enabledLiveNow) liveInOops()
                handleOopsLifeStart()
            }
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (needLife) handleOopsLifeStartOrStop(hasWindowFocus)
    }

    override fun onDetachedFromWindow() {
        if (needLife) {
            handleOopsLifeDestroy()
            appBarLayout!!.removeOnOffsetChangedListener(this)
            appBarLayout = null
            toolbar = null
        }
        super.onDetachedFromWindow()
    }

}
