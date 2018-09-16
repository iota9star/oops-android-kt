package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.ext.ctx

class OopsCardView : CardView, OopsLiveProvider {

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
        live = Oops.live(ctx, Oops.oops::cardViewBackgroundColor)
        live!!.observe(ctx, Observer(this::setBackgroundColor))
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
