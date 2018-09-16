package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.ext.ctx


class OopsDrawerLayout : DrawerLayout, OopsLiveProvider {

    constructor(context: Context) : super(context) {
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        registerOopsLive()
    }

    private var iconColor = 0
    private var arrowDrawable: DrawerArrowDrawable? = null
    private var live: LiveData<Int>? = null

    override fun registerOopsLive() {
        val ctx = this.ctx()
        live = Oops.live(ctx, Oops.oops::iconTitleActiveColor)
        live!!.observe(ctx, Observer {
            this.iconColor = it
            updateColor()
        })
    }

    override fun unregisterOopsLive() {
        live?.removeObservers(this.ctx())
        live = null
    }

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

    override fun onDetachedFromWindow() {
        unregisterOopsLive()
        super.onDetachedFromWindow()
    }
}