package io.nichijou.oops.temp

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live2
import io.nichijou.oops.widget.StatusBarMode


class StatusBarStateColor(@ColorInt val statusBarColor: Int, val statusBarMode: StatusBarMode) {
    companion object {
        fun live() = object : Live2<Int, StatusBarMode, StatusBarStateColor> {
            override fun apply(a: Int, b: StatusBarMode): StatusBarStateColor = StatusBarStateColor(a, b)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StatusBarStateColor

        if (statusBarColor != other.statusBarColor) return false
        if (statusBarMode != other.statusBarMode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = statusBarColor
        result = 31 * result + statusBarMode.hashCode()
        return result
    }

}
