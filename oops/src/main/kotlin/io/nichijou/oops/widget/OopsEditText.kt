package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.ext.ctx
import io.nichijou.oops.ext.liveMediator
import io.nichijou.oops.ext.tintAuto
import io.nichijou.oops.ext.tintCursor
import io.nichijou.oops.temp.IsDarkColor


class OopsEditText : AppCompatEditText, OopsLiveProvider {

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
    private var liveText: LiveData<Int>? = null
    private var liveHint: LiveData<Int>? = null

    @SuppressLint("ResourceType")
    override fun registerOopsLive() {
        if (attrs != null) {
            val ctx = this.ctx()
            val attrsArray = intArrayOf(android.R.attr.background, android.R.attr.textColor, android.R.attr.textColorHint)
            val ta = context.obtainStyledAttributes(attrs, attrsArray)
            val backgroundResId = ta.getResourceId(0, 0)
            val textColorResId = ta.getResourceId(1, 0)
            val textColorHintResId = ta.getResourceId(2, 0)
            ta.recycle()
            liveMediator = Oops.liveMediator(
                    Oops.liveColor(ctx, backgroundResId, Oops.live(ctx, Oops.oops::colorAccent))!!,
                    Oops.live(ctx, Oops.oops::isDark),
                    IsDarkColor.live())
            liveMediator!!.observe(ctx, Observer(this::updateColor))
            liveText = Oops.liveColor(ctx, textColorResId, null)
            liveText?.observe(ctx, Observer(this::setTextColor))
            liveHint = Oops.liveColor(ctx, textColorHintResId, null)
            liveHint?.observe(ctx, Observer(this::setHintTextColor))
        }
    }

    override fun unregisterOopsLive() {
        val ctx = this.ctx()
        liveMediator?.removeObservers(ctx)
        liveHint?.removeObservers(ctx)
        liveText?.removeObservers(ctx)
        liveMediator = null
        liveText = null
        liveHint = null
    }

    private fun updateColor(isDarkColor: IsDarkColor) {
        this.tintCursor(isDarkColor.color)
        this.tintAuto(isDarkColor.color, true, isDarkColor.isDark)
    }

    override fun onDetachedFromWindow() {
        unregisterOopsLive()
        super.onDetachedFromWindow()
    }
}
