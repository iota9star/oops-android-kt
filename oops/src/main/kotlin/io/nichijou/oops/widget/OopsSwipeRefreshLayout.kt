package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.ext.ctx
import io.nichijou.oops.ext.tintCircleBackground


class OopsSwipeRefreshLayout(context: Context, attrs: AttributeSet?) : SwipeRefreshLayout(context, attrs), OopsLiveProvider {

    init {
        registerOopsLive()
    }

    private var live: LiveData<Int>? = null

    override fun registerOopsLive() {
        val ctx = this.ctx()
        live = Oops.live(ctx, Oops.oops::swipeRefreshLayoutBackgroundColor)
        live!!.observe(ctx, Observer(this::tintCircleBackground))
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
