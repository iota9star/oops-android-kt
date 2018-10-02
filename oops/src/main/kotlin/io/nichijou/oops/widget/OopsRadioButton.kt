package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import android.util.SparseIntArray
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.resIds
import io.nichijou.oops.ext.tint

open class OopsRadioButton : AppCompatRadioButton, OopsViewLifeAndLive {

    private val ids: SparseIntArray

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        ids = context.resIds(attrs, intArrayOf(android.R.attr.background, android.R.attr.textColor))
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        ids = context.resIds(attrs, intArrayOf(android.R.attr.background, android.R.attr.textColor))
    }

    override fun howToLive() {
        oopsVM.isDarkColor(oopsVM.live(context, ids[android.R.attr.background], oopsVM.colorAccent)!!).observe(this, Observer {
            this.tint(it.color, it.isDark)
        })
        oopsVM.live(context, ids[android.R.attr.textColor])?.observe(this, Observer(this::setTextColor))
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
