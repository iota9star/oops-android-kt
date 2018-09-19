package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.resId
import io.nichijou.oops.ext.tintAuto
import io.nichijou.oops.ext.tintCursor
import io.nichijou.oops.temp.IsDarkColor


open class OopsTextInputEditText : TextInputEditText, OopsViewLifeAndLive {

    private val attrs: AttributeSet?

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
    }

    private var lastState: IsDarkColor? = null

    private fun updateColor(isDarkColor: IsDarkColor) {
        this.lastState = isDarkColor
        this.tintAuto(isDarkColor.color, true, isDarkColor.isDark)
        this.tintCursor(isDarkColor.color)
    }

    override fun refreshDrawableState() {
        super.refreshDrawableState()
        lastState?.let {
            post {
                updateColor(lastState!!)
            }
        }
    }

    override fun bindingLive() {
        ovm.isDarkColor(this.activity().resId(attrs, android.R.attr.background), ovm.colorAccent).observe(this, Observer(this::updateColor))
        ovm.textColorPrimary.observe(this, Observer(this::setTextColor))
        ovm.textColorSecondary.observe(this, Observer(this::setHintTextColor))
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
