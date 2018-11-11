package io.nichijou.oops.color

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live3

data class SnackbarColor(@ColorInt val textColor: Int, @ColorInt val actionColor: Int, @ColorInt val bgColor: Int) {
    companion object {
        fun live() = object : Live3<Int, Int, Int, SnackbarColor> {
            override fun apply(a: Int, b: Int, c: Int): SnackbarColor = SnackbarColor(a, b, c)
        }
    }
}