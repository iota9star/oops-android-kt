package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.*


class OopsTextInputLayout : TextInputLayout, OopsViewLifeAndLive {

    private val backgroundAttrName: String

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        backgroundAttrName = context.attrName(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundAttrName = context.attrName(attrs, android.R.attr.background)
    }

    override fun howToLive() {
        oopsVM.live(backgroundAttrName, oopsVM.colorAccent)!!.observe(this, Observer(this::setAccentColor))
        oopsVM.textColorSecondary.observe(this, Observer {
            this.setHintColor(it.adjustAlpha(0.7f))
        })
    }

    override fun getOopsViewModel(): OopsViewModel = oopsVM

    private val oopsVM = ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)

    private val oopsLife: LifecycleRegistry = LifecycleRegistry(this)

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
