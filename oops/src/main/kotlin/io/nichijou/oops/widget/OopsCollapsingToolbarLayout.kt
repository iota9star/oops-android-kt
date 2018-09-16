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
import io.nichijou.oops.OopsLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.R
import io.nichijou.oops.ext.*
import io.nichijou.oops.temp.ActiveColor
import io.nichijou.oops.temp.CollapsingToolbarStateColor


open class OopsCollapsingToolbarLayout : CollapsingToolbarLayout, OopsLifeAndLive {

    private val attrs: AttributeSet?

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
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
        stateColor?.let {
            val activeColor = it.active
            val collapsingColor = it.collapsingColor
            val maxOffset = appBarLayout!!.measuredHeight - toolbar!!.measuredHeight
            val ratio = lastOffset.toFloat() / maxOffset.toFloat()
            val bgColor = it.bgColor
            val blendedColor = collapsingColor.blendWith(bgColor, ratio)
            val expandedTitleColor = if (collapsingColor.isColorLight()) Color.BLACK else Color.WHITE
            val blendedTitleColor = expandedTitleColor.blendWith(activeColor, ratio)
            setCollapsedTitleTextColor(activeColor)
            setExpandedTitleColor(expandedTitleColor)
            tintMenu(toolbar!!, ActiveColor(blendedTitleColor, blendedColor.adjustAlpha(0.7f)))
        }
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
                        innerView.skipLive()
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

    override fun bindingLive() {
        val resId = this.activity().resId(attrs, R.attr.colorPrimary)
        ovm.collapsingToolbarStateColor(resId).observe(this, Observer {
            stateColor = it
            this.setContentScrimColor(it.bgColor)
            this.setStatusBarScrimColor(it.statusBarColor)
            if (appBarLayout != null && toolbar != null) {
                updateColor()
            }
        })
    }

    private val ovm by lazy {
        ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)
    }

    private val mViewLifecycleRegistry: LifecycleRegistry by lazy {
        LifecycleRegistry(this)
    }

    override fun getLifecycle(): Lifecycle = mViewLifecycleRegistry

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val viewParent = this.parent
        if (viewParent is AppBarLayout) {
            appBarLayout = viewParent
        }
        if (appBarLayout != null) {
            eachChildren(this)
            if (toolbar != null) {
                toolbar!!.skipLive()
                toolbar!!.setBackgroundColor(Color.TRANSPARENT)
                appBarLayout!!.addOnOffsetChangedListener(onOffsetChangedListener)
            }
        }
        bindingLive()
        mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        } else {
            mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        }
    }

    override fun onDetachedFromWindow() {
        mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        if (appBarLayout != null && toolbar != null) {
            appBarLayout!!.removeOnOffsetChangedListener(onOffsetChangedListener)
            appBarLayout = null
            toolbar = null
        }
        super.onDetachedFromWindow()
    }
}
