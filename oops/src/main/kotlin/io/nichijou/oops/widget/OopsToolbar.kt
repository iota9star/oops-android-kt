package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.R
import io.nichijou.oops.ext.*


open class OopsToolbar : Toolbar, OopsLifeAndLive {

    private val attrs: AttributeSet?

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
    }

    private var colorStateList: ColorStateList? = null

    override fun setNavigationIcon(icon: Drawable?) {
        if (icon == null) {
            super.setNavigationIcon(icon)
        } else {
            super.setNavigationIcon(icon.tint(colorStateList))
        }
    }

    private var skip = false
    fun skipLive(skip: Boolean = true) {
        this.skip = skip
    }

    override fun bindingLive() {
        ovm.live(this.activity().resId(attrs, R.attr.colorPrimary), ovm.colorPrimary)?.observe(this, Observer {
            if (!skip) {
                setBackgroundColor(it)
            }
        })
        ovm.activeColor.observe(this, Observer {
            this.setTitleTextColor(it.active)
            this.tintOverflowIcon(it.active)
            val sl = it.toEnabledSl()
            colorStateList = sl
            this.tintCollapseIcon(sl)
            this.tintNavIcon(sl)
            this.tintMenuItem(menu, it)
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
        mViewLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)// action view is late init, need hasWindowFocus = true
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
