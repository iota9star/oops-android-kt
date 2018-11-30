package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValue
import io.nichijou.oops.ext.tint

@SuppressLint("ViewConstructor")
class FloatingActionButton(context: Context, @Nullable attrs: AttributeSet?, private val enabledLiveNow: Boolean = true) : FloatingActionButton(context, attrs), OopsLifecycleOwner {

    private val backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.live(backgroundAttrValue, living.colorAccent)!!.observe(this, Observer(this::tint))
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