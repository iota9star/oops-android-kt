package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.color.IsDarkColor
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrName
import io.nichijou.oops.ext.oopsTint


class OopsTextInputEditText : TextInputEditText, OopsLifecycleOwner {

    private val backgroundAttrName: String

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        backgroundAttrName = context.attrName(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundAttrName = context.attrName(attrs, android.R.attr.background)
    }

    private var lastState: IsDarkColor? = null

    private fun updateColor(color: IsDarkColor) {
        this.lastState = color
        this.oopsTint(color)
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
        living.isDarkColor(living.live(backgroundAttrName, living.colorAccent)!!).observe(this, Observer(this::updateColor))
        living.textColorPrimary.observe(this, Observer(this::setTextColor))
        living.textColorSecondary.observe(this, Observer(this::setHintTextColor))
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
