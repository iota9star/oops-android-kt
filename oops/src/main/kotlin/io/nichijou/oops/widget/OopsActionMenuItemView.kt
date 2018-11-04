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
import io.nichijou.oops.ext.oopsTintIcon
import io.nichijou.oops.ext.tint

@SuppressLint("RestrictedApi")
internal class OopsActionMenuItemView : ActionMenuItemView, OopsLifecycleOwner {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var colorStateList: ColorStateList? = null

    private fun updateColor(color: Int) {
        this.setTextColor(color)
        colorStateList = PairColor(color).toEnabledSl()
        this.oopsTintIcon(colorStateList!!)
    }

    override fun setIcon(icon: Drawable?) {
        if (icon == null) {
            super.setIcon(icon)
        } else {
            super.setIcon(icon.tint(colorStateList))
        }
    }

    override fun liveInOops() {
        Oops.living(this.activity()).toolbarIconColor.observe(this, Observer(this::updateColor))
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
