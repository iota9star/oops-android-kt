package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValue
import io.nichijou.oops.ext.oopsTint

class OopsRatingBar : AppCompatRatingBar, OopsLifecycleOwner {

    private val backgroundAttrValue: String

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)
    }

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.isDarkColor(living.live(backgroundAttrValue, living.colorAccent)!!).observe(this, Observer(this::oopsTint))
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
