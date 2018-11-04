package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.*


class OopsTextInputLayout : TextInputLayout, OopsLifecycleOwner {

    private val backgroundAttrValue: String

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)
    }

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.live(backgroundAttrValue, living.colorAccent)!!.observe(this, Observer(this::setAccentColor))
        living.textColorSecondary.observe(this, Observer {
            this.setHintColor(it.adjustAlpha(0.7f))
        })
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
