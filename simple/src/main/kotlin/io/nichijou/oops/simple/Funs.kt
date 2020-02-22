package io.nichijou.oops.simple

import android.graphics.Color
import kotlin.random.Random

fun randomColor() = Color.rgb(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))

fun IntRange.random() = Random.nextInt(start, endInclusive + 1)

fun randomColors(range: IntRange): IntArray {
  val size = range.random()
  val intArray = IntArray(size)
  for (i in 0 until size) {
    intArray[i] = randomColor()
  }
  return intArray
}
