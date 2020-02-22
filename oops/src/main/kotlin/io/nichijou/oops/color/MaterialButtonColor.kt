package io.nichijou.oops.color

import io.nichijou.oops.ext.Live5

data class MaterialButtonColor(val textColor: Int?, val strokeColor: Int?, val rippleColor: Int?, val bgColor: Int?, val isDark: Boolean) {
  companion object {
    fun live() = object : Live5<Int, Int, Int, Int, Boolean, MaterialButtonColor> {
      override fun apply(a: Int?, b: Int?, c: Int?, d: Int?, e: Boolean?): MaterialButtonColor = MaterialButtonColor(a, b, c, d, e
        ?: false)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as MaterialButtonColor

    if (textColor != other.textColor) return false
    if (strokeColor != other.strokeColor) return false
    if (rippleColor != other.rippleColor) return false
    if (bgColor != other.bgColor) return false
    if (isDark != other.isDark) return false

    return true
  }

  override fun hashCode(): Int {
    var result = textColor ?: 0
    result = 31 * result + (strokeColor ?: 0)
    result = 31 * result + (rippleColor ?: 0)
    result = 31 * result + (bgColor ?: 0)
    result = 31 * result + isDark.hashCode()
    return result
  }
}
