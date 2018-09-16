package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.R
import io.nichijou.oops.ext.*
import io.nichijou.oops.temp.ActiveColor


class OopsToolbar : Toolbar, OopsLiveProvider {

    private val attrs: AttributeSet?

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        registerOopsLive()
    }

    private var colorStateList: ColorStateList? = null
    private var bgColor: LiveData<Int>? = null
    private var activeColor: MediatorLiveData<ActiveColor>? = null

    override fun registerOopsLive() {
        val ctx = this.ctx()
        bgColor = Oops.liveColor(ctx, ctx.resId(attrs, R.attr.colorPrimary), Oops.live(ctx, Oops.oops::colorPrimary))!!
        bgColor!!.observe(ctx, Observer {
            this.setBackgroundColor(it)
        })
        activeColor = Oops.liveMediator(
                Oops.live(ctx, Oops.oops::iconTitleActiveColor),
                Oops.live(ctx, Oops.oops::iconTitleInactiveColor),
                ActiveColor.live())
        activeColor!!.observe(ctx, Observer {
            this.setTitleTextColor(it.active)
            this.tintOverflowIcon(it.active)
            this.tintMenu(menu, it)
            val sl = it.toEnabledSl()
            colorStateList = sl
            this.tintCollapseIcon(sl)
            this.tintNavIcon(sl)
        })
    }

    override fun unregisterOopsLive() {
        val ctx = this.ctx()
        bgColor?.removeObservers(ctx)
        activeColor?.removeObservers(ctx)
        bgColor = null
        activeColor = null
    }

    override fun setNavigationIcon(icon: Drawable?) {
        if (icon == null) {
            super.setNavigationIcon(icon)
        } else {
            super.setNavigationIcon(icon.tint(colorStateList))
        }
    }

    override fun onDetachedFromWindow() {
        unregisterOopsLive()
        super.onDetachedFromWindow()
    }
}
