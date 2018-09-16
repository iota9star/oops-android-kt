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
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.R
import io.nichijou.oops.ext.*
import io.nichijou.oops.temp.ActiveColor
import io.nichijou.oops.temp.CollapsingToolbarStateColor


class OopsCollapsingToolbarLayout : CollapsingToolbarLayout, OopsLiveProvider {

    private val attrs: AttributeSet?

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        registerOopsLive()
    }

    private var live: MediatorLiveData<CollapsingToolbarStateColor>? = null
    override fun registerOopsLive() {
        val ctx = this.ctx()
        live = Oops.liveMediator(
                Oops.live(ctx, Oops.oops::iconTitleActiveColor),
                Oops.liveColor(ctx, ctx.resId(attrs, R.attr.colorPrimary), Oops.live(ctx, Oops.oops::colorPrimary))!!,
                Oops.live(ctx, Oops.oops::statusBarColor),
                Oops.live(ctx, Oops.oops::collapsingToolbarColor),
                CollapsingToolbarStateColor.live())
        live!!.observe(ctx, Observer {
            stateColor = it
            this.setContentScrimColor(it.bgColor)
            this.setStatusBarScrimColor(it.statusBarColor)
            if (appBarLayout != null && toolbar != null) {
                updateColor()
            }
        })
    }

    override fun unregisterOopsLive() {
        live?.removeObservers(this.ctx())
        live = null
    }

    private var appBarLayout: AppBarLayout? = null
    private var toolbar: OopsToolbar? = null
    private var lastOffset = 0
    private var stateColor: CollapsingToolbarStateColor? = null
    private val onOffsetChangedListener by lazy {
        AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (lastOffset != Math.abs(offset)) {
                updateColor()
            }
            lastOffset = Math.abs(offset)
        }
    }

    private fun updateColor() {
        val activeColor = stateColor!!.active
        val collapsingColor = stateColor!!.collapsingColor
        val maxOffset = appBarLayout!!.measuredHeight - toolbar!!.measuredHeight
        val ratio = lastOffset.toFloat() / maxOffset.toFloat()
        val bgColor = stateColor!!.bgColor
        val blendedColor = collapsingColor.blendWith(bgColor, ratio)
        val expandedTitleColor = if (collapsingColor.isColorLight()) Color.BLACK else Color.WHITE
        val blendedTitleColor = expandedTitleColor.blendWith(activeColor, ratio)
        setCollapsedTitleTextColor(activeColor)
        setExpandedTitleColor(expandedTitleColor)
        tintMenu(toolbar!!, ActiveColor(blendedTitleColor, blendedColor.adjustAlpha(0.7f)))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val viewParent = this.parent
        if (viewParent is AppBarLayout) {
            appBarLayout = viewParent
        }
        if (appBarLayout != null) {
            eachChildren(this)
            if (toolbar != null) {
                toolbar!!.unregisterOopsLive()
                toolbar!!.setBackgroundColor(Color.TRANSPARENT)
                appBarLayout!!.addOnOffsetChangedListener(onOffsetChangedListener)
            }
        }
    }

    override fun onDetachedFromWindow() {
        unregisterOopsLive()
        if (appBarLayout != null && toolbar != null) {
            appBarLayout!!.removeOnOffsetChangedListener(onOffsetChangedListener)
            appBarLayout = null
            toolbar = null
        }
        super.onDetachedFromWindow()
    }

    private fun eachChildren(view: ViewGroup) {
        for (i in 0 until view.childCount) {
            val child = view.getChildAt(i)
            if (child is OopsToolbar) {
                toolbar = child
                return
            } else if (child is ViewGroup) {
                eachChildren(child)
            }
        }
    }

    private fun tintMenu(toolbar: Toolbar, color: ActiveColor) {
        val sl = color.toEnabledSl()
        toolbar.tintNavIcon(sl)
        toolbar.tintCollapseIcon(sl)
        toolbar.tintOverflowIcon(color.active)
        val colorFilter = PorterDuffColorFilter(color.active, PorterDuff.Mode.SRC_IN)
        for (i in 0 until toolbar.childCount) {
            val v = toolbar.getChildAt(i)
            if (v is ActionMenuView) {
                for (j in 0 until v.childCount) {
                    val innerView = v.getChildAt(j)
                    if (innerView is OopsActionMenuItemView) {
                        innerView.unregisterOopsLive()
                        innerView.setTextColor(color.active)
                        val drawablesCount = innerView.compoundDrawables.size
                        for (k in 0 until drawablesCount) {
                            if (innerView.compoundDrawables[k] != null) {
                                innerView.compoundDrawables[k].colorFilter = colorFilter
                            }
                        }
                        innerView.tintIcon(sl)
                    }
                }
            }
        }
        toolbar.tintMenu(toolbar.menu ?: toolbar.menu, color)
    }
}
