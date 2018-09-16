package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.ext.ctx
import io.nichijou.oops.ext.isColorLight
import io.nichijou.oops.ext.resId
import io.nichijou.oops.ext.tintAuto


class OopsButton : AppCompatButton, OopsLiveProvider {

    private val attrs: AttributeSet?

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        registerOopsLive()
    }

    private var live: LiveData<Int>? = null

    override fun registerOopsLive() {
        val ctx = this.ctx()
        val resId = ctx.resId(attrs, android.R.attr.background)
        live = Oops.liveColor(ctx, resId, Oops.live(ctx, Oops.oops::colorAccent))
        live!!.observe(ctx, Observer {
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

    override fun unregisterOopsLive() {
        live?.removeObservers(this.ctx())
        live = null
    }

    override fun onDetachedFromWindow() {
        unregisterOopsLive()
        super.onDetachedFromWindow()
    }
}
