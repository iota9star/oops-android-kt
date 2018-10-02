package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.resId
import io.nichijou.oops.ext.tint

open class OopsRatingBar : AppCompatRatingBar, OopsViewLifeAndLive {

    private val backgroundResId: Int

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        backgroundResId = context.resId(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundResId = context.resId(attrs, android.R.attr.background)
    }

    override fun howToLive() {
        oopsVM.isDarkColor(oopsVM.live(context, backgroundResId, oopsVM.colorAccent)!!).observe(this, Observer {
            this.tint(it.color, it.isDark)
        })
    }

    override fun getOopsViewModel(): OopsViewModel = oopsVM

    private val oopsVM by lazy {
        ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)
    }

    private val oopsLife: LifecycleRegistry by lazy {
        LifecycleRegistry(this)
    }

    override fun getLifecycle(): Lifecycle = oopsLife

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startOopsLife()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        resumeOrPauseLife(hasWindowFocus)
    }

    override fun onDetachedFromWindow() {
        endOopsLife()
        super.onDetachedFromWindow()
    }
}