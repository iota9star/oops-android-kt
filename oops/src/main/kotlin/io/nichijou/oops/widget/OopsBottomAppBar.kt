package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.SparseArray
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomappbar.BottomAppBar
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.R
import io.nichijou.oops.color.ActiveColor
import io.nichijou.oops.ext.*

class OopsBottomAppBar : BottomAppBar, OopsViewLifeAndLive {

    private val attrNames: SparseArray<String>

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        attrNames = context.attrNames(attrs, intArrayOf(android.R.attr.background, com.google.android.material.R.attr.backgroundTint))
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrNames = context.attrNames(attrs, intArrayOf(android.R.attr.background, com.google.android.material.R.attr.backgroundTint))
    }

    private var colorStateList: ColorStateList? = null

    override fun setNavigationIcon(icon: Drawable?) {
        if (icon == null) {
            super.setNavigationIcon(icon)
        } else {
            super.setNavigationIcon(icon.tint(colorStateList))
        }
    }

    private fun updateColor(color: ActiveColor) {
        this.setTitleTextColor(color.active)
        this.oopsTintOverflowIcon(color.active)
        val sl = color.toEnabledSl()
        colorStateList = sl
        this.oopsTintCollapseIcon(sl)
        this.oopsTintNavIcon(sl)
        this.oopsTintMenuItem(menu, color)
    }

    override fun howToLive() {
        val backgroundTint = attrNames[R.attr.backgroundTint]
        val bgAttrName = if (backgroundTint.isNullOrBlank()) {
            attrNames[android.R.attr.background]
        } else {
            backgroundTint
        }
        oopsVM.live(bgAttrName)?.observe(this, Observer {
            val bg = this.background
            if (bg != null) {
                this.background = bg.tint(it)
            } else {
                setBackgroundColor(it)
            }
        })
        oopsVM.toolbarColor.observe(this, Observer(this::updateColor))
    }

    override fun getOopsViewModel(): OopsViewModel = oopsVM

    private val oopsVM by lazy {
        ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)
    }

    private val oopsLife: LifecycleRegistry by lazy {
        LifecycleRegistry(this)
    }

    override fun getLifecycle(): Lifecycle = oopsLife

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startOopsLife()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        resumeOrPauseLife(hasWindowFocus)
    }

    override fun onDetachedFromWindow() {
        endOopsLife()
        super.onDetachedFromWindow()
    }
}