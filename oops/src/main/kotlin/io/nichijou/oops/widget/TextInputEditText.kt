package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.color.IsDarkWithColor
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValue
import io.nichijou.oops.ext.tint


@SuppressLint("ViewConstructor")
class TextInputEditText(context: Context, @Nullable attrs: AttributeSet?, private val enabledLiveNow: Boolean = true) : TextInputEditText(context, attrs), OopsLifecycleOwner {

    private val backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)

    private var lastState: IsDarkWithColor? = null

    private fun updateColor(color: IsDarkWithColor) {
        this.lastState = color
        this.tint(color)
    }

    override fun refreshDrawableState() {
        super.refreshDrawableState()
        lastState?.let {
            post {
                updateColor(lastState!!)
            }
        }
    }

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.isDarkColor(living.live(backgroundAttrValue, living.colorAccent)!!).observe(this, Observer(this::updateColor))
        living.textColorPrimary.observe(this, Observer(this::setTextColor))
        living.textColorSecondary.observe(this, Observer(this::setHintTextColor))
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
