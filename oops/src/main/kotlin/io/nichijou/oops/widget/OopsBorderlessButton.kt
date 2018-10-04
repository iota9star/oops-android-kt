package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.adjustAlpha


open class OopsBorderlessButton : AppCompatButton, OopsViewLifeAndLive {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @SuppressLint("RestrictedApi")
    private fun updateColor(color: Int) {
        val textColorSl = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)), intArrayOf(color, color.adjustAlpha(0.56f)))
        this.setTextColor(textColorSl)
        isEnabled = !isEnabled
        isEnabled = !isEnabled
    }

    override fun howToLive() {
        oopsVM.colorAccent.observe(this, Observer(this::updateColor))
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
