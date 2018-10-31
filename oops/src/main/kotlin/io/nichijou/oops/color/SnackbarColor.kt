package io.nichijou.oops.color

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live3


class SnackbarColor(@ColorInt val textColor: Int, @ColorInt val actionColor: Int, @ColorInt val bgColor: Int) {

    companion object {
        fun live() = object : Live3<Int, Int, Int, SnackbarColor> {
            override fun apply(a: Int, b: Int, c: Int): SnackbarColor = SnackbarColor(a, b, c)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SnackbarColor

        if (textColor != other.textColor) return false
        if (actionColor != other.actionColor) return false
        if (bgColor != other.bgColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = textColor
        result = 31 * result + actionColor
        result = 31 * result + bgColor
        return result
    }


}
