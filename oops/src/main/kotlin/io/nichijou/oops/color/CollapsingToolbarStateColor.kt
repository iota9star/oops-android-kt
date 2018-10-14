package io.nichijou.oops.color

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live4


class CollapsingToolbarStateColor(@ColorInt val active: Int, @ColorInt val bgColor: Int, @ColorInt val statusBarColor: Int, @ColorInt val collapsingColor: Int) {
    companion object {
        fun live() = object : Live4<Int, Int, Int, Int, CollapsingToolbarStateColor> {
            override fun apply(a: Int, b: Int, c: Int, d: Int): CollapsingToolbarStateColor = CollapsingToolbarStateColor(a, b, c, d)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CollapsingToolbarStateColor

        if (active != other.active) return false
        if (bgColor != other.bgColor) return false
        if (statusBarColor != other.statusBarColor) return false
        if (collapsingColor != other.collapsingColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = active
        result = 31 * result + bgColor
        result = 31 * result + statusBarColor
        result = 31 * result + collapsingColor
        return result
    }

}
