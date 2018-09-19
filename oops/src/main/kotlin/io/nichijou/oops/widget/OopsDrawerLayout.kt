package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity


open class OopsDrawerLayout : DrawerLayout, OopsViewLifeAndLive {

    constructor(context: Context) : super(context)

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var iconColor = 0
    private var arrowDrawable: DrawerArrowDrawable? = null

    private fun updateColor() {
        if (iconColor == 0) return
        this.arrowDrawable?.let {
            it.color = iconColor
        }
    }

    override fun addDrawerListener(listener: DrawerLayout.DrawerListener) {
        super.addDrawerListener(listener)
        if (listener is ActionBarDrawerToggle) {
            this.arrowDrawable = listener.drawerArrowDrawable
            updateColor()
        }
    }

    override fun setDrawerListener(listener: DrawerLayout.DrawerListener) {
        super.setDrawerListener(listener)
        if (listener is ActionBarDrawerToggle) {
            this.arrowDrawable = listener.drawerArrowDrawable
            updateColor()
        }
    }

    override fun bindingLive() {
        ovm.iconTitleActiveColor.observe(this, Observer {
            this.iconColor = it
            updateColor()
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