package io.nichijou.oops.temp

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live5
import io.nichijou.oops.widget.TabLayoutBackgroundMode
import io.nichijou.oops.widget.TabLayoutIndicatorMode

class TabStateColor(@ColorInt val accent: Int, @ColorInt val primary: Int, @ColorInt val windowBackground: Int, val indicatorMode: TabLayoutIndicatorMode, val backgroundMode: TabLayoutBackgroundMode) {
    companion object {
        fun live() = object : Live5<Int, Int, Int, TabLayoutIndicatorMode, TabLayoutBackgroundMode, TabStateColor> {
            override fun apply(a: Int, b: Int, c: Int, d: TabLayoutIndicatorMode, e: TabLayoutBackgroundMode): TabStateColor = TabStateColor(a, b, c, d, e)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TabStateColor

        if (accent != other.accent) return false
        if (primary != other.primary) return false
        if (windowBackground != other.windowBackground) return false
        if (indicatorMode != other.indicatorMode) return false
        if (backgroundMode != other.backgroundMode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = accent
        result = 31 * result + primary
        result = 31 * result + windowBackground
        result = 31 * result + indicatorMode.hashCode()
        result = 31 * result + backgroundMode.hashCode()
        return result
    }

}