package io.nichijou.oops.temp

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live6
import io.nichijou.oops.widget.BottomNavigationViewBackgroundMode
import io.nichijou.oops.widget.BottomNavigationViewIconTextMode

class BottomNavStateColor(@ColorInt val accent: Int, @ColorInt val primary: Int, @ColorInt val primaryDark: Int, val isDark: Boolean, val iconTextMode: BottomNavigationViewIconTextMode, val backgroundMode: BottomNavigationViewBackgroundMode) {
    companion object {
        fun live() = object : Live6<Int, Int, Int, Boolean, BottomNavigationViewIconTextMode, BottomNavigationViewBackgroundMode, BottomNavStateColor> {
            override fun apply(a: Int, b: Int, c: Int, d: Boolean, e: BottomNavigationViewIconTextMode, f: BottomNavigationViewBackgroundMode): BottomNavStateColor = BottomNavStateColor(a, b, c, d, e, f)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BottomNavStateColor

        if (accent != other.accent) return false
        if (primary != other.primary) return false
        if (primaryDark != other.primaryDark) return false
        if (isDark != other.isDark) return false
        if (iconTextMode != other.iconTextMode) return false
        if (backgroundMode != other.backgroundMode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = accent
        result = 31 * result + primary
        result = 31 * result + primaryDark
        result = 31 * result + isDark.hashCode()
        result = 31 * result + iconTextMode.hashCode()
        result = 31 * result + backgroundMode.hashCode()
        return result
    }

}