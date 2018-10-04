package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrName
import io.nichijou.oops.ext.isColorLight
import io.nichijou.oops.ext.tintSelector


open class OopsButton : AppCompatButton, OopsViewLifeAndLive {

    private val backgroundAttrName: String

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        backgroundAttrName = context.attrName(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundAttrName = context.attrName(attrs, android.R.attr.background)
    }

    override fun howToLive() {
        oopsVM.isDarkColor(oopsVM.live(backgroundAttrName, oopsVM.colorAccent)!!).observe(this, Observer {
            val textColorSl = ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)),
                    intArrayOf(if (it.color.isColorLight()) Color.BLACK else Color.WHITE, if (it.isDark) Color.WHITE else Color.BLACK)
            )
            this.setTextColor(textColorSl)
            this.tintSelector(it.color, !it.color.isColorLight(), it.isDark)
            isEnabled = !isEnabled
            isEnabled = !isEnabled
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
