package io.nichijou.oops.color

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live4


data class CollapsingToolbarStateColor(@ColorInt val textColor: Int, @ColorInt val bgColor: Int, @ColorInt val statusBarColor: Int, @ColorInt val dominantColor: Int) {
    companion object {
        fun live() = object : Live4<Int, Int, Int, Int, CollapsingToolbarStateColor> {
            override fun apply(a: Int, b: Int, c: Int, d: Int): CollapsingToolbarStateColor = CollapsingToolbarStateColor(a, b, c, d)
        }
    }
}
