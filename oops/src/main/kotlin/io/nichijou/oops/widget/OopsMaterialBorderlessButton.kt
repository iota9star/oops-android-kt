package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.SparseArray
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.adjustAlpha
import io.nichijou.oops.ext.attrNames
import io.nichijou.oops.ext.tint
import io.nichijou.oops.temp.IsDarkColor


open class OopsMaterialBorderlessButton : MaterialButton, OopsViewLifeAndLive {

    private val attrNames: SparseArray<String>

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        attrNames = context.attrNames(attrs, intArrayOf(android.R.attr.background, com.google.android.material.R.attr.backgroundTint, com.google.android.material.R.attr.strokeColor))
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrNames = context.attrNames(attrs, intArrayOf(android.R.attr.background, com.google.android.material.R.attr.strokeColor))
    }

    @SuppressLint("RestrictedApi")
    private fun updateColor(color: IsDarkColor) {
        val textColorSl = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)), intArrayOf(color.color, color.color.adjustAlpha(0.56f)))
        this.setTextColor(textColorSl)
        this.icon = this.icon?.tint(textColorSl)
        isEnabled = !isEnabled
        isEnabled = !isEnabled
    }

    override fun howToLive() {
        oopsVM.isDarkColor(oopsVM.colorAccent).observe(this, Observer(this::updateColor))
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
