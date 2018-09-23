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
import io.nichijou.oops.R
import io.nichijou.oops.ext.*
import io.nichijou.oops.temp.ActiveColor
import io.nichijou.oops.temp.CollapsingToolbarStateColor


open class OopsCollapsingToolbarLayout : CollapsingToolbarLayout, OopsViewLifeAndLive, AppBarLayout.OnOffsetChangedListener {

    private val colorPrimaryResId: Int

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        colorPrimaryResId = context.resId(attrs, R.attr.colorPrimary)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        colorPrimaryResId = context.resId(attrs, R.attr.colorPrimary)
    }

    private var appBarLayout: AppBarLayout? = null
    private var toolbar: OopsToolbar? = null
    private var lastOffset = 0
    private var stateColor: CollapsingToolbarStateColor? = null

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, offset: Int) {
        if (lastOffset != Math.abs(offset)) {
            updateColor()
        }
        lastOffset = Math.abs(offset)
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
            tintMenu(toolbar!!, ActiveColor(blendedTitleColor, blendedColor.adjustAlpha(.7f)))
        }
    }

    private fun eachChildren(view: ViewGroup) {
        for (i in 0 until view.childCount) {
            val child = view.getChildAt(i)
            if (child is OopsToolbar) {
                toolbar = child
                return
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
                    val itemView = v.getChildAt(j)
                    if (itemView is OopsActionMenuItemView) {
                        itemView.unbindingLive()
                        itemView.setTextColor(color.active)
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
        toolbar.tintMenuItem(toolbar.menu ?: return, color)
    }

    override fun bindingLive() {
        ovm.collapsingToolbarStateColor(colorPrimaryResId).observe(this, Observer {
            stateColor = it
            this.setContentScrimColor(it.bgColor)
            this.setStatusBarScrimColor(it.statusBarColor)
            if (appBarLayout != null && toolbar != null) {
                updateColor()
            }
        })
    }

    override fun getOopsViewModel(): OopsViewModel = ovm

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
            toolbar?.apply {
                unbindingLive()
                setBackgroundColor(Color.TRANSPARENT)
                updateColor(ActiveColor(Color.WHITE, Color.GRAY.adjustAlpha(.4f)))
            }
            appBarLayout!!.addOnOffsetChangedListener(this)
        }
        bindingLive()
        mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        } else {
            mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        }
    }

    override fun onDetachedFromWindow() {
        mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        if (appBarLayout != null && toolbar != null) {
            appBarLayout!!.removeOnOffsetChangedListener(this)
            appBarLayout = null
            toolbar = null
        }
        super.onDetachedFromWindow()
    }
}
