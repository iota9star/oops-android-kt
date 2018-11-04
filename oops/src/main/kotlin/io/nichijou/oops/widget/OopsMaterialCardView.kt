package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.card.MaterialCardView
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValues

class OopsMaterialCardView : MaterialCardView, OopsLifecycleOwner {

    private val attrValues: SparseArray<String>

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        attrValues = context.attrValues(attrs, intArrayOf(com.google.android.material.R.attr.cardBackgroundColor, com.google.android.material.R.attr.strokeColor))
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrValues = context.attrValues(attrs, intArrayOf(com.google.android.material.R.attr.cardBackgroundColor, com.google.android.material.R.attr.strokeColor))
    }

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.live(attrValues[com.google.android.material.R.attr.cardBackgroundColor])?.observe(this, Observer(this::setCardBackgroundColor))
        living.live(attrValues[com.google.android.material.R.attr.strokeColor])?.observe(this, Observer(this::setStrokeColor))
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
