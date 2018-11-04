package io.nichijou.oops.color

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live2
import io.nichijou.oops.widget.StatusBarMode


data class StatusBarStateColor(@ColorInt val statusBarColor: Int, val statusBarMode: StatusBarMode) {
    companion object {
        fun live() = object : Live2<Int, StatusBarMode, StatusBarStateColor> {
            override fun apply(a: Int, b: StatusBarMode): StatusBarStateColor = StatusBarStateColor(a, b)
        }
    }
}
