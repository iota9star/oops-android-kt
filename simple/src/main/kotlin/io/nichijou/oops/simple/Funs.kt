package io.nichijou.oops.simple

import android.graphics.Color
import io.nichijou.oops.font.OopsFont
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

val fonts = mutableListOf("", "1.ttf", "2.ttf", "3.ttf", "4.ttf")
var fontIndex = 0
fun changeFont() {
    OopsFont.assetPath = fonts[fontIndex]
    if (fontIndex == fonts.size - 1) {
        fontIndex = 0
    }
    fontIndex++
}