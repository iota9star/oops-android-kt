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
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.resId
import io.nichijou.oops.ext.tintAuto
import io.nichijou.oops.ext.tintCursor
import io.nichijou.oops.temp.IsDarkColor


open class OopsTextInputEditText : TextInputEditText, OopsViewLifeAndLive {

    private val backgroundResId: Int

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        backgroundResId = context.resId(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundResId = context.resId(attrs, android.R.attr.background)
    }

    private var lastState: IsDarkColor? = null

    private fun updateColor(isDarkColor: IsDarkColor) {
        this.lastState = isDarkColor
        this.tintAuto(isDarkColor.color, true, isDarkColor.isDark)
        this.tintCursor(isDarkColor.color)
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
        oopsVM.isDarkColor(oopsVM.live(context, backgroundResId, oopsVM.colorAccent)!!).observe(this, Observer(this::updateColor))
        oopsVM.textColorPrimary.observe(this, Observer(this::setTextColor))
        oopsVM.textColorSecondary.observe(this, Observer(this::setHintTextColor))
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
