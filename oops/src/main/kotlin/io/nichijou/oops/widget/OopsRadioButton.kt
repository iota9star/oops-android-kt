package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.ext.ctx
import io.nichijou.oops.ext.liveMediator
import io.nichijou.oops.ext.resId
import io.nichijou.oops.ext.tint
import io.nichijou.oops.temp.IsDarkColor

class OopsRadioButton : AppCompatRadioButton, OopsLiveProvider {

    private val attrs: AttributeSet?

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        registerOopsLive()
    }

    private var liveMediator: MediatorLiveData<IsDarkColor>? = null
    private var live: LiveData<Int>? = null

    override fun registerOopsLive() {
        val ctx = this.ctx()
        val backgroundResId = ctx.resId(attrs, android.R.attr.background)
        liveMediator = Oops.liveMediator(
                Oops.liveColor(ctx, backgroundResId, Oops.live(ctx, Oops.oops::colorAccent))!!,
                Oops.live(ctx, Oops.oops::isDark),
                IsDarkColor.live())
        liveMediator!!.observe(ctx, Observer {
            this.tint(it.color, it.isDark)
        })
        val textColorResId = ctx.resId(attrs, android.R.attr.textColor)
        live = Oops.liveColor(ctx, textColorResId, null)
        live?.observe(ctx, Observer(this::setTextColor))
    }

    override fun unregisterOopsLive() {
        live?.removeObservers(this.ctx())
        liveMediator?.removeObservers(this.ctx())
        live = null
        liveMediator = null
    }

    override fun onDetachedFromWindow() {
        unregisterOopsLive()
        super.onDetachedFromWindow()
    }
}
