package io.nichijou.oops.color

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live2

class IsDarkColor(@ColorInt val color: Int, val isDark: Boolean) {
    companion object {
        fun live() = object : Live2<Int, Boolean, IsDarkColor> {
            override fun apply(a: Int, b: Boolean): IsDarkColor = IsDarkColor(a, b)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IsDarkColor

        if (color != other.color) return false
        if (isDark != other.isDark) return false

        return true
    }

    override fun hashCode(): Int {
        var result = color
        result = 31 * result + isDark.hashCode()
        return result
    }
}