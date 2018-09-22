package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.*


open class OopsTextInputLayout : TextInputLayout, OopsViewLifeAndLive {

    private val backgroundResId: Int

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        backgroundResId = context.resId(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundResId = context.resId(attrs, android.R.attr.background)
    }

    override fun bindingLive() {
        ovm.live(backgroundResId, ovm.colorAccent)?.observe(this, Observer(this::setAccentColor))
        ovm.textColorSecondary.observe(this, Observer {
            this.setHintColor(it.adjustAlpha(0.7f))
        })
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
