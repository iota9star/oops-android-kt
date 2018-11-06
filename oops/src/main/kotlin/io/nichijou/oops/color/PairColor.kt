package io.nichijou.oops.color

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live2
import io.nichijou.oops.ext.adjustAlpha


data class PairColor(@ColorInt val first: Int, @ColorInt val second: Int = first.adjustAlpha(.7f)) {
    fun toEnabledSl() = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)), intArrayOf(first, second))
    fun toSelectedSl() = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf(-android.R.attr.state_selected)), intArrayOf(first, second))

    companion object {
        fun live() = object : Live2<Int, Int, PairColor> {
            override fun apply(a: Int, b: Int): PairColor = PairColor(a, b)
        }
    }
}
