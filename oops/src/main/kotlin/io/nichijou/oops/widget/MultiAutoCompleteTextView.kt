package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValues
import io.nichijou.oops.ext.tint


@SuppressLint("ViewConstructor")
class MultiAutoCompleteTextView(context: Context, @Nullable attrs: AttributeSet?, private val enabledLiveNow: Boolean = true) : AppCompatMultiAutoCompleteTextView(context, attrs), OopsLifecycleOwner {

    private val attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, android.R.attr.textColor, android.R.attr.textColorHint))

    @SuppressLint("ResourceType")
    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.isDarkColor(living.live(attrValues[android.R.attr.background], living.colorAccent)!!).observe(this, Observer(this::tint))
        living.live(attrValues[android.R.attr.textColor])?.observe(this, Observer(this::setTextColor))
        living.live(attrValues[android.R.attr.textColorHint])?.observe(this, Observer(this::setHighlightColor))
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
