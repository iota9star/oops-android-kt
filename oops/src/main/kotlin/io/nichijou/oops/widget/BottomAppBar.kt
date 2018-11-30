package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.bottomappbar.BottomAppBar
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
class BottomAppBar(context: Context, @Nullable attrs: AttributeSet?, private val enabledLiveNow: Boolean = true) : BottomAppBar(context, attrs), OopsLifecycleOwner {

    private val attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, R.attr.backgroundTint, R.attr.titleTextColor, R.attr.subtitleTextColor))

    private var colorStateList: ColorStateList? = null

    override fun setNavigationIcon(icon: Drawable?) {
        super.setNavigationIcon(icon?.tint(colorStateList))
    }

    private fun updateColor(color: Int) {
        val active = PairColor(color)
        colorStateList = active.toEnabledSl().also {
            this.tintCollapseIcon(it)
            this.tintNavIcon(it)
        }
        this.tintOverflowIcon(color)
        this.tintMenuItem(menu, active)
    }

    override fun liveInOops() {
        val backgroundTint = attrValues[R.attr.backgroundTint]
        val bgAttrValue = if (backgroundTint.isNullOrBlank()) {
            attrValues[android.R.attr.background]
        } else {
            backgroundTint
        }
        val living = Oops.living(this.activity())
        living.live(bgAttrValue, living.colorPrimary)!!.observe(this, Observer {
            val bg = this.background
            if (bg != null) {
                this.background = bg.tint(it)
            } else {
                setBackgroundColor(it)
            }
        })
        living.live(attrValues[androidx.appcompat.R.attr.titleTextColor], living.toolbarTitleColor)!!.observe(this, Observer(this::setTitleTextColor))
        living.live(attrValues[androidx.appcompat.R.attr.subtitleTextColor], living.toolbarSubtitleColor)!!.observe(this, Observer(this::setSubtitleTextColor))

        living.toolbarIconColor.observe(this, Observer(this::updateColor))
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