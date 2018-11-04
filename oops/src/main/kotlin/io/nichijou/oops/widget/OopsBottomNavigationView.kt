package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValue


class OopsBottomNavigationView : BottomNavigationView, OopsLifecycleOwner {

    private val backgroundAttrValue: String

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundAttrValue = context.attrValue(attrs, android.R.attr.background)
    }

    private fun updateColor(pairColor: PairColor) {
        val sl = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(pairColor.first, pairColor.second))
        itemIconTintList = sl
        itemTextColor = sl
    }

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.live(backgroundAttrValue, living.colorPrimary)!!.observe(this, Observer(this::setBackgroundColor))
        living.bottomNavStateColor.observe(this, Observer(this::updateColor))
    }

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        attachOopsLife()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        handleOopsLifeStartOrStop(hasWindowFocus)
    }

    override fun onDetachedFromWindow() {
        detachOopsLife()
        super.onDetachedFromWindow()
    }
}