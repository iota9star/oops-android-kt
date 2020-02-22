package io.nichijou.oops.color

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live2NonNull
import io.nichijou.utils.adjustAlpha

data class PairColor(@ColorInt val first: Int, @ColorInt val second: Int = first.adjustAlpha(.7f)) {
  fun toEnabledSl() = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)), intArrayOf(first, second))
  fun toSelectedSl() = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf(-android.R.attr.state_selected)), intArrayOf(first, second))
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as PairColor

    if (first != other.first) return false
    if (second != other.second) return false

    return true
  }

  override fun hashCode(): Int {
    var result = first
    result = 31 * result + second
    return result
  }

  companion object {
    fun live() = object : Live2NonNull<Int, Int, PairColor> {
      override fun apply(a: Int, b: Int): PairColor = PairColor(a, b)
    }
  }
}
