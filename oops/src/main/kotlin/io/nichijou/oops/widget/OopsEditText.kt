package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrNames
import io.nichijou.oops.ext.tintAuto
import io.nichijou.oops.ext.tintCursor
import io.nichijou.oops.temp.IsDarkColor


open class OopsEditText : AppCompatEditText, OopsViewLifeAndLive {

    private val attrNames: SparseArray<String>

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        attrNames = context.attrNames(attrs, intArrayOf(android.R.attr.background, android.R.attr.textColor, android.R.attr.textColorHint))
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrNames = context.attrNames(attrs, intArrayOf(android.R.attr.background, android.R.attr.textColor))
    }

    private fun updateColor(isDarkColor: IsDarkColor) {
        this.tintCursor(isDarkColor.color)
        this.tintAuto(isDarkColor.color, true, isDarkColor.isDark)
    }

    @SuppressLint("ResourceType")
    override fun howToLive() {
        oopsVM.isDarkColor(oopsVM.live(attrNames[android.R.attr.background], oopsVM.colorAccent)!!).observe(this, Observer(this::updateColor))
        oopsVM.live(attrNames[android.R.attr.textColor])?.observe(this, Observer(this::setTextColor))
        oopsVM.live(attrNames[android.R.attr.textColorHint])?.observe(this, Observer(this::setHighlightColor))
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
