package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.tint
import io.nichijou.oops.ext.tintIcon

@SuppressLint("RestrictedApi", "ViewConstructor")
internal class ActionMenuItemView(context: Context, @Nullable attrs: AttributeSet?, private val enabledLiveNow: Boolean = true) : ActionMenuItemView(context, attrs), OopsLifecycleOwner {

    private var colorStateList: ColorStateList? = null

    private fun updateColor(color: Int) {
        this.setTextColor(color)
        colorStateList = PairColor(color).toEnabledSl()
        this.tintIcon(colorStateList!!)
    }

    override fun setIcon(icon: Drawable?) {
        super.setIcon(icon?.tint(colorStateList))
    }

    override fun liveInOops() {
        Oops.living(this.activity()).toolbarIconColor.observe(this, Observer(this::updateColor))
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
