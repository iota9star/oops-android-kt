package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.SparseArray
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.R
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.ext.*


class OopsToolbar : Toolbar, OopsLifecycleOwner {

    private val attrValues: SparseArray<String>

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, R.attr.titleTextColor, R.attr.subtitleTextColor))
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, R.attr.titleTextColor, R.attr.subtitleTextColor))
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
        living.live(attrValues[android.R.attr.background], living.colorPrimary)!!.observe(this, Observer(this::setBackgroundColor))
        living.live(attrValues[R.attr.titleTextColor], living.toolbarTitleColor)!!.observe(this, Observer(this::setTitleTextColor))
        living.live(attrValues[R.attr.subtitleTextColor], living.toolbarSubtitleColor)!!.observe(this, Observer(this::setSubtitleTextColor))

        living.toolbarIconColor.observe(this, Observer(this::updateColor))
    }

    fun updateColor(color: Int) {
        this.oopsTintOverflowIcon(color)
        val active = PairColor(color)
        colorStateList = active.toEnabledSl()
        this.oopsTintCollapseIcon(colorStateList!!)
        this.oopsTintNavIcon(colorStateList!!)
        this.oopsTintMenuItem(menu, active)
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
