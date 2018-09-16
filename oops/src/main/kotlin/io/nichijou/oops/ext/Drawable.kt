package io.nichijou.oops.ext

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

@CheckResult
fun Drawable.tint(@ColorInt color: Int): Drawable {
    var d: Drawable = this
    d = DrawableCompat.wrap(d.mutate())
    DrawableCompat.setTintMode(d, PorterDuff.Mode.SRC_IN)
    DrawableCompat.setTint(d, color)
    return d
}

@CheckResult
fun Drawable.tint(sl: ColorStateList?): Drawable {
    if (sl == null) return this
    var d: Drawable = this
    d = DrawableCompat.wrap(d.mutate())
    DrawableCompat.setTintList(d, sl)
    return d
}