package io.nichijou.oops.widget

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
import io.nichijou.oops.ext.*


class OopsCollapsingToolbarLayout : CollapsingToolbarLayout, OopsLifecycleOwner, AppBarLayout.OnOffsetChangedListener {

    private val backgroundAttrValue: String

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)
    }

    private var appBarLayout: AppBarLayout? = null
    private var toolbar: OopsToolbar? = null
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
            if (child is OopsToolbar) {
                toolbar = child
                return
            } else if (child is ViewGroup) eachChildren(child)
        }
    }

    private fun tintMenu(toolbar: Toolbar, color: PairColor) {
        toolbar.apply {
            val sl = color.toEnabledSl()
            oopsTintNavIcon(sl)
            oopsTintCollapseIcon(sl)
            oopsTintOverflowIcon(color.first)
            val colorFilter = PorterDuffColorFilter(color.first, PorterDuff.Mode.SRC_IN)
            for (i in 0 until this.childCount) {
                val v = getChildAt(i)
                if (v is ActionMenuView) {
                    for (j in 0 until v.childCount) {
                        val itemView = v.getChildAt(j)
                        if (itemView is OopsActionMenuItemView) {
                            itemView.detachOopsLife()
                            itemView.setTextColor(color.first)
                            val drawablesCount = itemView.compoundDrawables.size
                            for (k in 0 until drawablesCount) {
                                if (itemView.compoundDrawables[k] != null) {
                                    itemView.compoundDrawables[k].colorFilter = colorFilter
                                }
                            }
                            itemView.oopsTintIcon(sl)
                        }
                    }
                }
            }
            oopsTintMenuItem(this.menu ?: return, color)
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
                    detachOopsLife()
                    setBackgroundColor(Color.TRANSPARENT)
                    updateColor(Color.WHITE)
                }
                appBarLayout!!.addOnOffsetChangedListener(this)
                attachOopsLife()
            }
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (needLife) handleOopsLifeStartOrStop(hasWindowFocus)
    }

    override fun onDetachedFromWindow() {
        if (needLife) {
            detachOopsLife()
            appBarLayout!!.removeOnOffsetChangedListener(this)
            appBarLayout = null
            toolbar = null
        }
        super.onDetachedFromWindow()
    }
}
