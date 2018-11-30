package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.adjustAlpha
import io.nichijou.oops.ext.attrValues
import io.nichijou.oops.ext.tint


@SuppressLint("ViewConstructor")
class MaterialBorderlessButton(context: Context, @Nullable attrs: AttributeSet?, private val enabledLiveNow: Boolean = true) : MaterialButton(context, attrs), OopsLifecycleOwner {

    private val attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, com.google.android.material.R.attr.strokeColor))

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.isDarkColor(living.live(attrValues[android.R.attr.background], living.colorAccent)!!).observe(this, Observer {
            val textColorSl = ColorStateList(arrayOf(
                intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)),
                intArrayOf(it.color, it.color.adjustAlpha(.56f)))
            this.setTextColor(textColorSl)
            this.icon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.icon?.tint(textColorSl)
            } else {
                if (isEnabled) {
                    this.icon?.tint(it.color)
                } else {
                    this.icon?.tint(it.color.adjustAlpha(.56f))
                }
            }
            this.background?.let { d ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && d is RippleDrawable) {
                    d.setColor(ColorStateList.valueOf(it.color.adjustAlpha(.56f)))
                }
            }
            isEnabled = !isEnabled
            isEnabled = !isEnabled
        })
        living.live(attrValues[com.google.android.material.R.attr.strokeColor])?.observe(this, Observer {
            this.strokeColor = ColorStateList.valueOf(it)
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
