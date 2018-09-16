package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.ext.adjustAlpha
import io.nichijou.oops.ext.ctx
import io.nichijou.oops.ext.liveMediator
import io.nichijou.oops.ext.tint
import io.nichijou.oops.temp.ActiveColor
import io.nichijou.oops.temp.TabStateColor


class OopsTabLayout : TabLayout, OopsLiveProvider {

    constructor(context: Context) : super(context) {
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        registerOopsLive()
    }

    private var liveActive: MediatorLiveData<ActiveColor>? = null
    private var liveTab: MediatorLiveData<TabStateColor>? = null

    override fun registerOopsLive() {
        val ctx = this.ctx()
        liveActive = Oops.liveMediator(
                Oops.live(ctx, Oops.oops::iconTitleActiveColor),
                Oops.live(ctx, Oops.oops::iconTitleInactiveColor),
                ActiveColor.live())
        liveActive!!.observe(ctx, Observer {
            this.tintIcon(it.active)
            this.setTabTextColors(it.active, it.inactive)
        })
        liveTab = Oops.liveMediator(
                Oops.live(ctx, Oops.oops::colorAccent),
                Oops.live(ctx, Oops.oops::colorPrimary),
                Oops.live(ctx, Oops.oops::windowBackground),
                Oops.live(ctx, Oops.oops::tabLayoutIndicatorMode),
                Oops.live(ctx, Oops.oops::tabLayoutBackgroundMode),
                TabStateColor.live())
        liveTab!!.observe(ctx, Observer {
            when (it.indicatorMode) {
                TabLayoutIndicatorMode.ACCENT -> this.setSelectedTabIndicatorColor(it.accent)
                TabLayoutIndicatorMode.PRIMARY -> this.setSelectedTabIndicatorColor(it.primary)
            }
            when (it.backgroundMode) {
                TabLayoutBackgroundMode.ACCENT -> this.setBackgroundColor(it.accent)
                TabLayoutBackgroundMode.PRIMARY -> this.setBackgroundColor(it.primary)
                TabLayoutBackgroundMode.WINDOW_BACKGROUND -> this.setBackgroundColor(it.windowBackground)
            }
        })
    }

    override fun unregisterOopsLive() {
        val ctx = this.ctx()
        liveActive?.removeObservers(ctx)
        liveTab?.removeObservers(ctx)
        liveTab = null
        liveActive = null
    }

    private fun tintIcon(color: Int) {
        val sl = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_selected), intArrayOf(android.R.attr.state_selected)),
                intArrayOf(color.adjustAlpha(0.5f), color))
        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            if (tab != null && tab.icon != null) {
                tab.icon = tab.icon?.tint(sl)
            }
        }
    }

    override fun onDetachedFromWindow() {
        unregisterOopsLive()
        super.onDetachedFromWindow()
    }
}

enum class TabLayoutIndicatorMode {
    ACCENT, PRIMARY
}

enum class TabLayoutBackgroundMode {
    ACCENT,
    PRIMARY,
    WINDOW_BACKGROUND
}