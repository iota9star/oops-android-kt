package io.nichijou.oops.widget

import android.annotation.SuppressLint
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
import io.nichijou.oops.R
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValues
import io.nichijou.oops.ext.tint
import io.nichijou.oops.ext.tintCollapseIcon
import io.nichijou.oops.ext.tintMenuItem
import io.nichijou.oops.ext.tintNavIcon
import io.nichijou.oops.ext.tintOverflowIcon


@SuppressLint("ViewConstructor")
class Toolbar(context: Context, @Nullable attrs: AttributeSet?, private val enabledLiveNow: Boolean = true) : Toolbar(context, attrs), OopsLifecycleOwner {

    private val attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, R.attr.titleTextColor, R.attr.subtitleTextColor))

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
            this.tintCollapseIcon(it)
            this.tintNavIcon(it)
        }
        this.tintOverflowIcon(color)
        this.tintMenuItem(menu, active)
    }

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (enabledLiveNow) liveInOops()
        handleOopsLifeStart()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        handleOopsLifeStartOrStop(hasWindowFocus)
    }

    override fun onDetachedFromWindow() {
        handleOopsLifeDestroy()
        super.onDetachedFromWindow()
    }

}
