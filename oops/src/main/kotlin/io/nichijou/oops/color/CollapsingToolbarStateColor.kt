package io.nichijou.oops.color

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live4NonNull

data class CollapsingToolbarStateColor(@ColorInt val titleColor: Int, @ColorInt val bgColor: Int, @ColorInt val statusBarColor: Int, @ColorInt val dominantColor: Int) {
  companion object {
    fun live() = object : Live4NonNull<Int, Int, Int, Int, CollapsingToolbarStateColor> {
      override fun apply(a: Int, b: Int, c: Int, d: Int): CollapsingToolbarStateColor = CollapsingToolbarStateColor(a, b, c, d)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as CollapsingToolbarStateColor

    if (titleColor != other.titleColor) return false
    if (bgColor != other.bgColor) return false
    if (statusBarColor != other.statusBarColor) return false
    if (dominantColor != other.dominantColor) return false

    return true
  }

  override fun hashCode(): Int {
    var result = titleColor
    result = 31 * result + bgColor
    result = 31 * result + statusBarColor
    result = 31 * result + dominantColor
    return result
  }
}
