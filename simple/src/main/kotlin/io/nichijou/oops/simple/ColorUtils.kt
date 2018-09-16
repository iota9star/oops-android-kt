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