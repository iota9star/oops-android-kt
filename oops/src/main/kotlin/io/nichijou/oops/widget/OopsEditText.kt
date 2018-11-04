package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValues
import io.nichijou.oops.ext.oopsTint


class OopsEditText : AppCompatEditText, OopsLifecycleOwner {

    private val attrValues: SparseArray<String>

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, android.R.attr.textColor, android.R.attr.textColorHint))
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, android.R.attr.textColor))
    }

    @SuppressLint("ResourceType")
    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.isDarkColor(living.live(attrValues[android.R.attr.background], living.colorAccent)!!).observe(this, Observer(this::oopsTint))
        living.live(attrValues[android.R.attr.textColor])?.observe(this, Observer(this::setTextColor))
        living.live(attrValues[android.R.attr.textColorHint])?.observe(this, Observer(this::setHighlightColor))
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
