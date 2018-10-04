package io.nichijou.oops.simple

import android.graphics.Color
import java.util.*

fun randomColor(): Int {
    val random = Random()
    var r = Integer.toHexString(random.nextInt(256)).toUpperCase()
    var g = Integer.toHexString(random.nextInt(256)).toUpperCase()
    var b = Integer.toHexString(random.nextInt(256)).toUpperCase()

    r = if (r.length == 1) "0$r" else r
    g = if (g.length == 1) "0$g" else g
    b = if (b.length == 1) "0$b" else b

    return Color.parseColor("#$r$g$b")
}

fun randomColors(range: IntRange): IntArray {
    val size = Random().nextInt(range.last - range.first) + range.first
    val intArray = IntArray(size)
    for (i in 0 until size) {
        intArray[i] = randomColor()
    }
    return intArray
}