package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.SparseArray
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.*


open class OopsMaterialBorderlessButton : MaterialButton, OopsViewLifeAndLive {

    private val attrNames: SparseArray<String>

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        attrNames = context.attrNames(attrs, intArrayOf(android.R.attr.background, com.google.android.material.R.attr.strokeColor))
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrNames = context.attrNames(attrs, intArrayOf(android.R.attr.background, com.google.android.material.R.attr.strokeColor))
    }

    override fun howToLive() {
        oopsVM.isDarkColor(oopsVM.live(attrNames[android.R.attr.background], oopsVM.colorAccent)!!).observe(this, Observer {
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
                this.setBackgroundCompat(d)
            }
            isEnabled = !isEnabled
            isEnabled = !isEnabled
        })
        oopsVM.live(attrNames[com.google.android.material.R.attr.strokeColor])?.observe(this, Observer {
            this.strokeColor = ColorStateList.valueOf(it)
        })
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
