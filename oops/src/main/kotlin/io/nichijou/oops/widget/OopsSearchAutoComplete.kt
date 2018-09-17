package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.adjustAlpha
import io.nichijou.oops.ext.tintCursor

@SuppressLint("RestrictedApi")
class OopsSearchAutoComplete : SearchView.SearchAutoComplete, OopsLifeAndLive {
    private val attrs: AttributeSet?

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
    }

    override fun bindingLive() {
        ovm.iconTitleActiveColor.observe(this, Observer {
            this.tintCursor(it)
            this.setTextColor(it)
            this.setHintTextColor(it.adjustAlpha(0.7f))
        })
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