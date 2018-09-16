package io.nichijou.oops.ext

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build

internal fun PackageManager.getAppIcon(packageName: String): Bitmap? {
    try {
        val drawable = getApplicationIcon(packageName)
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && drawable is AdaptiveIconDrawable) {
            val backgroundDr = drawable.background
            val foregroundDr = drawable.foreground

            val drr = arrayOfNulls<Drawable>(2)
            drr[0] = backgroundDr
            drr[1] = foregroundDr

            val layerDrawable = LayerDrawable(drr)

            val width = layerDrawable.intrinsicWidth
            val height = layerDrawable.intrinsicHeight

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(bitmap)

            layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
            layerDrawable.draw(canvas)
            return bitmap
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return null
}