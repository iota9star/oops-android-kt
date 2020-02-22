package io.nichijou.oops.color

import androidx.annotation.ColorInt
import io.nichijou.oops.ext.Live2NonNull
import io.nichijou.oops.widget.BarMode

data class BarStateColor(@ColorInt val color: Int, val mode: BarMode) {
  companion object {
    fun live() = object : Live2NonNull<Int, BarMode, BarStateColor> {
      override fun apply(a: Int, b: BarMode): BarStateColor = BarStateColor(a, b)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as BarStateColor

    if (color != other.color) return false
    if (mode != other.mode) return false

    return true
  }

  override fun hashCode(): Int {
    var result = color
    result = 31 * result + mode.hashCode()
    return result
  }
}
