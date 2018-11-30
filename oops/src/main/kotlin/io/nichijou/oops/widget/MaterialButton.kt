package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValues
import io.nichijou.oops.ext.isColorLight
import io.nichijou.oops.ext.tint


@SuppressLint("ViewConstructor")
class MaterialButton(context: Context, @Nullable attrs: AttributeSet?, private val enabledLiveNow: Boolean = true) : MaterialButton(context, attrs), OopsLifecycleOwner {

    private val attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, com.google.android.material.R.attr.strokeColor))

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.isDarkColor(living.live(attrValues[android.R.attr.background], living.colorAccent)!!).observe(this, Observer {
            this.tint(it.color, !it.color.isColorLight(), it.isDark)
            isEnabled = !isEnabled
            isEnabled = !isEnabled
        })
        living.live(attrValues[com.google.android.material.R.attr.strokeColor])?.observe(this, Observer {
            strokeColor = ColorStateList.valueOf(it)
            isEnabled = !isEnabled
            isEnabled = !isEnabled
        })
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
