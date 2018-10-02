package io.nichijou.oops.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import io.nichijou.oops.R
import io.nichijou.oops.ext.adjustAlpha
import io.nichijou.oops.ext.shiftColor
import io.nichijou.oops.ext.stripAlpha
import io.nichijou.oops.ext.tint
import io.nichijou.oops.temp.ActiveColor
import java.lang.reflect.Field


object TintUtils {

    fun tintSwitchDrawable(@NonNull context: Context, @NonNull from: Drawable, @ColorInt tint: Int, thumb: Boolean, compatSwitch: Boolean, useDarker: Boolean): Drawable? {
        var color = tint
        if (useDarker) {
            color = color.shiftColor(1.1f)
        }
        color = color.adjustAlpha(if (compatSwitch && !thumb) 0.5f else 1.0f)
        val disabled: Int
        var normal: Int
        if (thumb) {
            disabled = ContextCompat.getColor(context,
                    if (useDarker) R.color.md_switch_thumb_disabled_dark else R.color.md_switch_thumb_disabled_light)
            normal = ContextCompat.getColor(context,
                    if (useDarker) R.color.md_switch_thumb_normal_dark else R.color.md_switch_thumb_normal_light)
        } else {
            disabled = ContextCompat.getColor(context,
                    if (useDarker) R.color.md_switch_track_disabled_dark else R.color.md_switch_track_disabled_light)
            normal = ContextCompat.getColor(context,
                    if (useDarker) R.color.md_switch_track_normal_dark else R.color.md_switch_track_normal_light)
        }

        // Stock switch includes its own alpha
        if (!compatSwitch) {
            normal = normal.stripAlpha()
        }

        val sl = ColorStateList(arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_activated, -android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_enabled, android.R.attr.state_activated),
                intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked)),
                intArrayOf(disabled, normal, color, color))
        return from.tint(sl)
    }

    @Throws(Exception::class)
    fun tintImageViewDrawable(target: Any, field: Field, color: ActiveColor) {
        field.isAccessible = true
        val imageView = field.get(target) as ImageView
        if (imageView.drawable != null) {
            imageView.setImageDrawable(imageView.drawable.tint(color.toEnabledSl()))
        }
        field.isAccessible = false
    }

    @SuppressLint("PrivateResource")
    @ColorInt
    fun getDefaultRippleColor(context: Context, useDarkRipple: Boolean): Int = ContextCompat.getColor(context, if (useDarkRipple) R.color.ripple_material_light else R.color.ripple_material_dark)

    fun getDisabledColorStateList(@ColorInt normal: Int, @ColorInt disabled: Int): ColorStateList = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled)), intArrayOf(disabled, normal))

}