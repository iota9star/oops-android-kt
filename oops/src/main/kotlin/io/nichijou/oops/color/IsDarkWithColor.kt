package io.nichijou.oops.color

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live2

data class IsDarkWithColor(@ColorInt val color: Int, val isDark: Boolean) {
    companion object {
        fun live() = object : Live2<Int, Boolean, IsDarkWithColor> {
            override fun apply(a: Int, b: Boolean): IsDarkWithColor = IsDarkWithColor(a, b)
        }
    }
}