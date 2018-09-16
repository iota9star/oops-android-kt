package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.ext.ctx
import io.nichijou.oops.ext.liveMediator
import io.nichijou.oops.ext.tint
import io.nichijou.oops.ext.tintIcon
import io.nichijou.oops.temp.ActiveColor

@SuppressLint("RestrictedApi")
class OopsActionMenuItemView : ActionMenuItemView, OopsLiveProvider {

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
    private var activeColor: MediatorLiveData<ActiveColor>? = null

    override fun registerOopsLive() {
        val ctx = this.ctx()
        activeColor = Oops.liveMediator(
                Oops.live(ctx, Oops.oops::iconTitleActiveColor),
                Oops.live(ctx, Oops.oops::iconTitleInactiveColor),
                ActiveColor.live())
        activeColor!!.observe(ctx, Observer(this::updateColor))
    }

    override fun unregisterOopsLive() {
        activeColor?.removeObservers(this.ctx())
        activeColor = null
    }

    private fun updateColor(lastActive: ActiveColor) {
        this.setTextColor(lastActive.active)
        val sl = lastActive.toEnabledSl()
        this.tintIcon(sl)
        colorStateList = sl
    }

    override fun setIcon(icon: Drawable?) {
        if (icon == null) {
            super.setIcon(icon)
        } else {
            super.setIcon(icon.tint(colorStateList))
        }
    }

    override fun onDetachedFromWindow() {
        unregisterOopsLive()
        super.onDetachedFromWindow()
    }
}
