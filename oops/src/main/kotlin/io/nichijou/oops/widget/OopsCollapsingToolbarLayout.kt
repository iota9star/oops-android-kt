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
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.color.ActiveColor
import io.nichijou.oops.color.CollapsingToolbarStateColor
import io.nichijou.oops.ext.*


class OopsCollapsingToolbarLayout : CollapsingToolbarLayout, OopsViewLifeAndLive, AppBarLayout.OnOffsetChangedListener {

    private val backgroundAttrName: String

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        backgroundAttrName = context.attrName(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundAttrName = context.attrName(attrs, android.R.attr.background)
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
            val blendedColor = collapsingColor.blendWith(bgColor, ratio)
            val expandedTitleColor = if (collapsingColor.isColorLight()) Color.BLACK else Color.WHITE
            val blendedTitleColor = expandedTitleColor.blendWith(active, ratio)
            setCollapsedTitleTextColor(active)
            setExpandedTitleColor(expandedTitleColor)
            tintMenu(toolbar!!, ActiveColor(blendedTitleColor, blendedColor.adjustAlpha(.64f)))
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

    private fun tintMenu(toolbar: Toolbar, color: ActiveColor) {
        val sl = color.toEnabledSl()
        toolbar.oopsTintNavIcon(sl)
        toolbar.oopsTintCollapseIcon(sl)
        toolbar.oopsTintOverflowIcon(color.active)
        val colorFilter = PorterDuffColorFilter(color.active, PorterDuff.Mode.SRC_IN)
        for (i in 0 until toolbar.childCount) {
            val v = toolbar.getChildAt(i)
            if (v is ActionMenuView) {
                for (j in 0 until v.childCount) {
                    val itemView = v.getChildAt(j)
                    if (itemView is OopsActionMenuItemView) {
                        itemView.endOopsLife()
                        itemView.setTextColor(color.active)
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
        toolbar.oopsTintMenuItem(toolbar.menu ?: return, color)
    }

    override fun howToLive() {
        oopsVM.collapsingToolbarStateColor(oopsVM.live(backgroundAttrName, oopsVM.colorPrimary)!!).observe(this, Observer {
            stateColor = it
            this.setContentScrimColor(it.bgColor)
            this.setStatusBarScrimColor(it.statusBarColor)
            if (appBarLayout != null && toolbar != null) {
                updateColor()
            }
        })
    }

    override fun getOopsViewModel(): OopsViewModel = oopsVM

    private val oopsVM = ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)

    private val oopsLife: LifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = oopsLife

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
                    endOopsLife()
                    setBackgroundColor(Color.TRANSPARENT)
                    updateColor(ActiveColor(Color.WHITE, Color.GRAY.adjustAlpha(.4f)))
                }
                appBarLayout!!.addOnOffsetChangedListener(this)
                startOopsLife()
            }
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (needLife) resumeOrPauseLife(hasWindowFocus)
    }

    override fun onDetachedFromWindow() {
        if (needLife) {
            endOopsLife()
            appBarLayout!!.removeOnOffsetChangedListener(this)
            appBarLayout = null
            toolbar = null
        }
        super.onDetachedFromWindow()
    }
}
