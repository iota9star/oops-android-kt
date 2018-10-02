package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.R
import io.nichijou.oops.ext.*
import io.nichijou.oops.temp.ActiveColor


open class OopsToolbar : Toolbar, OopsViewLifeAndLive {

    private val colorPrimaryResId: Int

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        colorPrimaryResId = context.resId(attrs, R.attr.colorPrimary)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        colorPrimaryResId = context.resId(attrs, R.attr.colorPrimary)
    }

    private var colorStateList: ColorStateList? = null

    override fun setNavigationIcon(icon: Drawable?) {
        if (icon == null) {
            super.setNavigationIcon(icon)
        } else {
            super.setNavigationIcon(icon.tint(colorStateList))
        }
    }

    override fun howToLive() {
        oopsVM.live(context, colorPrimaryResId, oopsVM.colorPrimary)!!.observe(this, Observer(this::setBackgroundColor))
        oopsVM.activeColor.observe(this, Observer(this::updateColor))
    }

    fun updateColor(color: ActiveColor) {
        this.setTitleTextColor(color.active)
        this.tintOverflowIcon(color.active)
        val sl = color.toEnabledSl()
        colorStateList = sl
        this.tintCollapseIcon(sl)
        this.tintNavIcon(sl)
        this.tintMenuItem(menu, color)
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
