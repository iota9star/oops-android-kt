package io.nichijou.oops.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

@ColorInt
fun Int.colorRes(context: Context): Int = ContextCompat.getColor(context, this)

fun Int.drawableRes(context: Context): Drawable? = ContextCompat.getDrawable(context, this)

fun Int.colorStateListRes(context: Context): ColorStateList? = ContextCompat.getColorStateList(context, this)
