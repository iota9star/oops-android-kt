package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.SparseArray
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.R
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValues
import io.nichijou.oops.ext.tint


class OopsTabLayout : TabLayout, OopsLifecycleOwner {

    private val attrValues: SparseArray<String>

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, R.attr.tabTextColor, R.attr.tabIndicatorColor, R.attr.tabSelectedTextColor))
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrValues = context.attrValues(attrs, intArrayOf(android.R.attr.background, R.attr.tabTextColor, R.attr.tabIndicatorColor, R.attr.tabSelectedTextColor))
    }

    override fun liveInOops() {
        val living = Oops.living(this.activity())
        living.live(attrValues[android.R.attr.background], living.colorPrimary)!!.observe(this, Observer(this::setBackgroundColor))
        living.live(attrValues[R.attr.tabIndicatorColor], living.colorAccent)!!.observe(this, Observer(this::setSelectedTabIndicatorColor))

        living.tabTextIconColor(
            living.live(attrValues[R.attr.tabTextColor], living.tabLayoutTextColor)!!,
            living.live(attrValues[R.attr.tabSelectedTextColor], living.tabLayoutSelectedTextColor)!!).observe(this, Observer {
            this.setTabTextColors(it.first, it.second)
            this.updateIconColor(it)
        })
    }

    private var iconColor: ColorStateList? = null

    internal fun getIconColor() = iconColor

    private fun updateIconColor(color: PairColor) {
        iconColor = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_selected), intArrayOf(android.R.attr.state_selected)),
            intArrayOf(color.first, color.second)
        )
        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            if (tab != null && tab.icon != null) {
                tab.icon = tab.icon!!.tint(iconColor)
            }
        }
        val slidingTabIndicator = this.getChildAt(0) as ViewGroup
        var tabView: ViewGroup
        for (i in 0 until slidingTabIndicator.childCount) {
            tabView = slidingTabIndicator.getChildAt(i) as ViewGroup
            (tabView.getChildAt(0) as OopsTabImageView).apply {
                setTabTextColor(iconColor)
                setImageDrawable(drawable)
            }
        }
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