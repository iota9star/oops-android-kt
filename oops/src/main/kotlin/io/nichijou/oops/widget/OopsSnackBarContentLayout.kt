package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.SnackbarContentLayout
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.ext.ctx

@SuppressLint("RestrictedApi")
class OopsSnackBarContentLayout : SnackbarContentLayout, OopsLiveProvider {

    constructor(context: Context) : super(context) {
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        registerOopsLive()
    }

    private var live: LiveData<Int>? = null

    override fun registerOopsLive() {
        val ctx = this.ctx()
        live = Oops.live(ctx, Oops.oops::snackBarBackgroundColor)
        live!!.observe(ctx, Observer(this::updateColor))
    }

    private fun updateColor(color: Int) {
        setBackgroundColor(color)
        postDelayed({
            val parent = this.parent
            if (parent != null && parent is FrameLayout) {
                parent.setBackgroundColor(color)
            }
        }, 42)// SnackbarLayout init after this view
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