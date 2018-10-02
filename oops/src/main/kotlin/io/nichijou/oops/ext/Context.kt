package io.nichijou.oops.ext

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.util.SparseIntArray
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

fun Context.getStatusBarHeight() = this.resources.getDimensionPixelSize(this.resources.getIdentifier("status_bar_height", "dimen", "android"))

fun Context.getScreenHeight(): Int = this.resources.displayMetrics.heightPixels

fun Context.getScreenWidth(): Int = this.resources.displayMetrics.widthPixels

fun Context.dp2px(dp: Float): Int = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.resources.displayMetrics) + 0.5f).toInt()

fun Context.px2dp(px: Float): Int = (px / this.resources.displayMetrics.density + 0.5f).toInt()

fun Context.px2sp(px: Float): Int = (px / this.resources.displayMetrics.scaledDensity + 0.5f).toInt()

fun Context.sp2px(sp: Float): Int = (sp * this.resources.displayMetrics.scaledDensity + 0.5f).toInt()

fun Context.getDialogWidth(): Int = this.resources.displayMetrics.widthPixels - 100

fun Context.activity(): AppCompatActivity {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is AppCompatActivity) {
            return ctx
        }
        ctx = ctx.baseContext
    }
    throw IllegalStateException("no context ...")
}

fun Context.resId(@AttrRes attr: Int, fallback: Int = 0): Int {
    val a = theme.obtainStyledAttributes(intArrayOf(attr))
    try {
        return a.getResourceId(0, fallback)
    } finally {
        a.recycle()
    }
}

fun Context.resId(attrs: AttributeSet?, @AttrRes attrId: Int): Int {
    if (attrs == null) return -1
    val ta = obtainStyledAttributes(attrs, intArrayOf(attrId))
    try {
        return ta.getResourceId(0, -1)
    } finally {
        ta.recycle()
    }
}

fun Context.resIds(attrs: AttributeSet?, attrIds: IntArray): SparseIntArray {
    val ids = SparseIntArray(attrIds.size)
    if (attrs == null) return ids
    val ta = obtainStyledAttributes(attrs, attrIds)
    try {
        for ((i, v) in attrIds.withIndex()) {
            ids.put(v, ta.getResourceId(i, -1))
        }
    } finally {
        ta.recycle()
    }
    return ids
}

fun Context.colorAttr(@AttrRes attr: Int, fallback: Int): Int {
    val a = this.theme.obtainStyledAttributes(intArrayOf(attr))
    return try {
        a.getColor(0, fallback)
    } catch (e: Exception) {
        loge(e) { "obtain styled attributes error..." }
        fallback
    } finally {
        a.recycle()
    }
}

fun Context.colorAttr(@AttrRes attr: Int): Int = this.colorAttr(attr, -1)

fun Context.colorRes(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun Context.drawableRes(@DrawableRes resId: Int) = ContextCompat.getDrawable(this, resId)