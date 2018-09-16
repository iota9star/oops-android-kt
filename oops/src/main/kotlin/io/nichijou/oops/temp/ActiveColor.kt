package io.nichijou.oops.temp

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live2


class ActiveColor(@ColorInt val active: Int, @ColorInt val inactive: Int) {
    fun toEnabledSl() = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)), intArrayOf(active, inactive))
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ActiveColor

        if (active != other.active) return false
        if (inactive != other.inactive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = active
        result = 31 * result + inactive
        return result
    }

    companion object {
        fun live() = object : Live2<Int, Int, ActiveColor> {
            override fun apply(a: Int, b: Int): ActiveColor = ActiveColor(a, b)
        }
    }

}
