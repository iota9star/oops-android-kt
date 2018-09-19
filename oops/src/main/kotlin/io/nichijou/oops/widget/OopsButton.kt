package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.isColorLight
import io.nichijou.oops.ext.resId
import io.nichijou.oops.ext.tintAuto


open class OopsButton : AppCompatButton, OopsViewLifeAndLive {

    private val attrs: AttributeSet?

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
    }

    override fun bindingLive() {
        val resId = this.activity().resId(attrs, android.R.attr.background)
        ovm.live(resId, ovm.colorAccent)?.observe(this, Observer {
            val isLight = it.isColorLight()
            val stateList = ColorStateList(
                    arrayOf(
                            intArrayOf(android.R.attr.state_enabled),
                            intArrayOf(-android.R.attr.state_enabled)),
                    intArrayOf(
                            if (isLight) Color.BLACK else Color.WHITE,
                            if (isLight) Color.WHITE else Color.BLACK))
            this.setTextColor(stateList)
            this.tintAuto(it, true, !isLight)
        })
    }

    private val ovm by lazy {
        ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)
    }

    override fun getOopsViewModel(): OopsViewModel = ovm

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
