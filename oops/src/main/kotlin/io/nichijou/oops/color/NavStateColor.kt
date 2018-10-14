package io.nichijou.oops.color

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live4
import io.nichijou.oops.widget.NavigationViewTintMode

class NavStateColor(@ColorInt val accent: Int, @ColorInt val primary: Int, val isDark: Boolean, val mode: NavigationViewTintMode) {
    companion object {
        fun live() = object : Live4<Int, Int, Boolean, NavigationViewTintMode, NavStateColor> {
            override fun apply(a: Int, b: Int, c: Boolean, d: NavigationViewTintMode): NavStateColor = NavStateColor(a, b, c, d)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NavStateColor

        if (accent != other.accent) return false
        if (primary != other.primary) return false
        if (isDark != other.isDark) return false
        if (mode != other.mode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = accent
        result = 31 * result + primary
        result = 31 * result + isDark.hashCode()
        result = 31 * result + mode.hashCode()
        return result
    }

}