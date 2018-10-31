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
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.color.ActiveColor
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.oopsTintIcon
import io.nichijou.oops.ext.tint

@SuppressLint("RestrictedApi")
internal class OopsActionMenuItemView : ActionMenuItemView, OopsViewLifeAndLive {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var colorStateList: ColorStateList? = null

    private fun updateColor(lastActive: ActiveColor) {
        this.setTextColor(lastActive.active)
        val sl = lastActive.toEnabledSl()
        this.oopsTintIcon(sl)
        colorStateList = sl
    }

    override fun setIcon(icon: Drawable?) {
        if (icon == null) {
            super.setIcon(icon)
        } else {
            super.setIcon(icon.tint(colorStateList))
        }
    }

    override fun howToLive() {
        oopsVM.toolbarColor.observe(this, Observer(this::updateColor))
    }

    override fun getOopsViewModel(): OopsViewModel = oopsVM

    private val oopsVM = ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)

    private val oopsLife: LifecycleRegistry = LifecycleRegistry(this)

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
