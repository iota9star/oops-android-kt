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
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.color.ActiveColor
import io.nichijou.oops.ext.*


class OopsToolbar : Toolbar, OopsLifecycleOwner {

    private val backgroundAttrName: String

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        backgroundAttrName = context.attrName(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundAttrName = context.attrName(attrs, android.R.attr.background)
    }

    private var colorStateList: ColorStateList? = null

    override fun setNavigationIcon(icon: Drawable?) {
        if (icon == null) {
            super.setNavigationIcon(icon)
        } else {
            super.setNavigationIcon(icon.tint(colorStateList))
        }
    }

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.live(backgroundAttrName, living.colorPrimary)!!.observe(this, Observer(this::setBackgroundColor))
        living.toolbarColor.observe(this, Observer(this::updateColor))
    }

    fun updateColor(color: ActiveColor) {
        this.setTitleTextColor(color.active)
        this.oopsTintOverflowIcon(color.active)
        val sl = color.toEnabledSl()
        colorStateList = sl
        this.oopsTintCollapseIcon(sl)
        this.oopsTintNavIcon(sl)
        this.oopsTintMenuItem(menu, color)
    }

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        attachOopsLife()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        handleOopsLifeStartOrStop(hasWindowFocus)
    }

    override fun onDetachedFromWindow() {
        detachOopsLife()
        super.onDetachedFromWindow()
    }
}
