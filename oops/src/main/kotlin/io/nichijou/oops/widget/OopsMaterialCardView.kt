package io.nichijou.oops.widget

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.card.MaterialCardView
import io.nichijou.oops.OopsViewLifeAndLive
import io.nichijou.oops.OopsViewModel
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrNames

open class OopsMaterialCardView : MaterialCardView, OopsViewLifeAndLive {

    private val attrNames: SparseArray<String>

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        attrNames = context.attrNames(attrs, intArrayOf(com.google.android.material.R.attr.cardBackgroundColor, com.google.android.material.R.attr.strokeColor))
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrNames = context.attrNames(attrs, intArrayOf(com.google.android.material.R.attr.cardBackgroundColor, com.google.android.material.R.attr.strokeColor))
    }

    override fun howToLive() {
        oopsVM.live(attrNames[com.google.android.material.R.attr.cardBackgroundColor])?.observe(this, Observer(this::setCardBackgroundColor))
        oopsVM.live(attrNames[com.google.android.material.R.attr.strokeColor])?.observe(this, Observer(this::setStrokeColor))
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
