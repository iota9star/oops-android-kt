package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.resId

open class OopsTextView : AppCompatTextView, OopsViewLifeAndLive {

    private val textColorResId: Int

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        textColorResId = context.resId(attrs, android.R.attr.textColor)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        textColorResId = context.resId(attrs, android.R.attr.textColor)
    }

    override fun bindingLive() {
        ovm.live(textColorResId, if (id == android.R.id.title) ovm.textColorPrimary else ovm.textColorSecondary)
                ?.observe(this, Observer(this::setTextColor))
    }

    override fun getOopsViewModel(): OopsViewModel = ovm

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
            mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        } else {
            mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        }
    }

    override fun onDetachedFromWindow() {
        mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onDetachedFromWindow()
    }
}
