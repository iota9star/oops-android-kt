package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.tintAuto
import io.nichijou.oops.ext.tintCursor
import io.nichijou.oops.temp.IsDarkColor


open class OopsEditText : AppCompatEditText, OopsLifeAndLive {

    private val attrs: AttributeSet?

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
    }

    private fun updateColor(isDarkColor: IsDarkColor) {
        this.tintCursor(isDarkColor.color)
        this.tintAuto(isDarkColor.color, true, isDarkColor.isDark)
    }

    @SuppressLint("ResourceType")
    override fun bindingLive() {
        if (attrs != null) {
            val attrsArray = intArrayOf(android.R.attr.background, android.R.attr.textColor, android.R.attr.textColorHint)
            val ta = context.obtainStyledAttributes(attrs, attrsArray)
            val backgroundResId = ta.getResourceId(0, 0)
            val textColorResId = ta.getResourceId(1, 0)
            val textColorHintResId = ta.getResourceId(2, 0)
            ta.recycle()
            ovm.live(textColorResId)?.observe(this, Observer(this::setTextColor))
            ovm.live(textColorHintResId)?.observe(this, Observer(this::setHighlightColor))
            ovm.isDarkColor(backgroundResId, ovm.colorAccent).observe(this, Observer(this::updateColor))
        }
    }

    private val ovm by lazy {
        ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)
    }

    private val mViewLifecycleRegistry: LifecycleRegistry by lazy {
        LifecycleRegistry(this)
    }

    override fun getLifecycle(): Lifecycle = mViewLifecycleRegistry

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        bindingLive()
        mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        } else {
            mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        }
    }

    override fun onDetachedFromWindow() {
        mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onDetachedFromWindow()
    }
}
