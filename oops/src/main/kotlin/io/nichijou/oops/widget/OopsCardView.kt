package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValue

class OopsCardView : CardView, OopsLifecycleOwner {

    private val backgroundAttrValue: String

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        backgroundAttrValue = context.attrValue(attrs, androidx.cardview.R.attr.cardBackgroundColor)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundAttrValue = context.attrValue(attrs, androidx.cardview.R.attr.cardBackgroundColor)
    }

    override fun liveInOops() {
        Oops.living(this.activity()).live(backgroundAttrValue)?.observe(this, Observer(this::setCardBackgroundColor))
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
