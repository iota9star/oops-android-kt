package io.nichijou.oops.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.ext.*
import io.nichijou.oops.temp.IsDarkColor

class OopsFloatingActionButton : FloatingActionButton, OopsLiveProvider {

    private val attrs: AttributeSet?

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        registerOopsLive()
    }

    private var live: MediatorLiveData<IsDarkColor>? = null

    override fun registerOopsLive() {
        val ctx = this.ctx()
        val resId = ctx.resId(attrs, android.R.attr.background)
        live = Oops.liveMediator(
                Oops.liveColor(ctx, resId, Oops.live(ctx, Oops.oops::colorAccent))!!,
                Oops.live(ctx, Oops.oops::isDark),
                IsDarkColor.live())
        live!!.observe(ctx, Observer {
            val isDark = !it.color.isColorLight()
            this.setImageDrawable(drawable.tint(if (isDark) Color.WHITE else Color.BLACK))
            this.tintAuto(it.color, true, it.isDark)
        })
    }

    override fun unregisterOopsLive() {
        live?.removeObservers(this.ctx())
        live = null
    }

    override fun onDetachedFromWindow() {
        unregisterOopsLive()
        super.onDetachedFromWindow()
    }
}