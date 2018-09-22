package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import android.util.SparseIntArray
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.resIds
import io.nichijou.oops.ext.tint

open class OopsCheckBox : AppCompatCheckBox, OopsViewLifeAndLive {

    private val ids: SparseIntArray

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        ids = context.resIds(attrs, intArrayOf(android.R.attr.background, android.R.attr.textColor))
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        ids = context.resIds(attrs, intArrayOf(android.R.attr.background, android.R.attr.textColor))
    }

    override fun bindingLive() {
        ovm.isDarkColor(ids[android.R.attr.background], ovm.colorAccent).observe(this, Observer {
            this.tint(it.color, it.isDark)
        })
        ovm.live(ids[android.R.attr.textColor])?.observe(this, Observer(this::setTextColor))
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
