package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValue

class OopsTextView : AppCompatTextView, OopsLifecycleOwner {

    private val textColorAttrValue: String

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        textColorAttrValue = context.attrValue(attrs, android.R.attr.textColor)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        textColorAttrValue = context.attrValue(attrs, android.R.attr.textColor)
    }

    override fun liveInOops() {
        Oops.living(this.activity()).live(textColorAttrValue)?.observe(this, Observer(this::setTextColor))
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
