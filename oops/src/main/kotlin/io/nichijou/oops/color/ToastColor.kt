package io.nichijou.oops.color

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live2


class ToastColor(@ColorInt val textColor: Int, @ColorInt val bgColor: Int) {

    companion object {
        fun live() = object : Live2<Int, Int, ToastColor> {
            override fun apply(a: Int, b: Int): ToastColor = ToastColor(a, b)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ToastColor

        if (textColor != other.textColor) return false
        if (bgColor != other.bgColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = textColor
        result = 31 * result + bgColor
        return result
    }

}
