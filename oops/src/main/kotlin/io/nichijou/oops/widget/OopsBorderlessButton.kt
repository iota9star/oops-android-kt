package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.ext.adjustAlpha
import io.nichijou.oops.ext.ctx


class OopsBorderlessButton : AppCompatButton, OopsLiveProvider {

    constructor(context: Context) : super(context) {
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        registerOopsLive()
    }

    private var live: LiveData<Int>? = null

    override fun registerOopsLive() {
        val ctx = this.ctx()
        live = Oops.live(ctx, Oops.oops::colorAccent)
        live!!.observe(ctx, Observer(this::updateColor))
    }

    private fun updateColor(color: Int) {
        val textColorSl = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)), intArrayOf(color, color.adjustAlpha(0.56f)))
        this.setTextColor(textColorSl)
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
