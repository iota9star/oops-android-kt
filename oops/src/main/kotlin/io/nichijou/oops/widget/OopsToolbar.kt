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
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValues
import io.nichijou.oops.ext.oopsTintCollapseIcon
import io.nichijou.oops.ext.oopsTintMenuItem
import io.nichijou.oops.ext.oopsTintNavIcon
import io.nichijou.oops.ext.oopsTintOverflowIcon
import io.nichijou.oops.ext.tint


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
        super.setNavigationIcon(icon?.tint(colorStateList))
    }

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.live(attrValues[android.R.attr.background], living.colorPrimary)!!.observe(this, Observer(this::setBackgroundColor))
        living.live(attrValues[R.attr.titleTextColor], living.toolbarTitleColor)!!.observe(this, Observer(this::setTitleTextColor))
        living.live(attrValues[R.attr.subtitleTextColor], living.toolbarSubtitleColor)!!.observe(this, Observer(this::setSubtitleTextColor))

        living.toolbarIconColor.observe(this, Observer(this::updateColor))
    }

    fun updateColor(color: Int) {
        val active = PairColor(color)
        colorStateList = active.toEnabledSl().also {
            this.oopsTintCollapseIcon(it)
            this.oopsTintNavIcon(it)
        }
        this.oopsTintOverflowIcon(color)
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
