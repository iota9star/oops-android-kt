package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.color.IsDarkColor
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrName
import io.nichijou.oops.ext.oopsTint


class OopsTextInputEditText : TextInputEditText, OopsViewLifeAndLive {

    private val backgroundAttrName: String

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        backgroundAttrName = context.attrName(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundAttrName = context.attrName(attrs, android.R.attr.background)
    }

    private var lastState: IsDarkColor? = null

    private fun updateColor(color: IsDarkColor) {
        this.lastState = color
        this.oopsTint(color)
    }

    override fun refreshDrawableState() {
        super.refreshDrawableState()
        lastState?.let {
            post {
                updateColor(lastState!!)
            }
        }
    }

    override fun howToLive() {
        oopsVM.isDarkColor(oopsVM.live(backgroundAttrName, oopsVM.colorAccent)!!).observe(this, Observer(this::updateColor))
        oopsVM.textColorPrimary.observe(this, Observer(this::setTextColor))
        oopsVM.textColorSecondary.observe(this, Observer(this::setHintTextColor))
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
