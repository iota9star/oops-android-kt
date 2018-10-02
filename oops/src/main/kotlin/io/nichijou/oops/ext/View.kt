package io.nichijou.oops.ext

import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.TintableBackgroundView
import androidx.core.view.ViewCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.nichijou.oops.R
import io.nichijou.oops.temp.ActiveColor
import io.nichijou.oops.temp.IsDarkColor
import io.nichijou.oops.utils.TintUtils


fun View.getAbsoluteX(): Float {
    var x = this.x
    val p = this.parent
    if (p != null && p is View) {
        x += p.getAbsoluteX()
    }
    return x
}


fun View.getAbsoluteY(): Float {
    var y = this.y
    val p = this.parent
    if (p != null && p is View) {
        y += p.getAbsoluteY()
    }
    return y
}

fun View.activity(): AppCompatActivity {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    throw IllegalStateException("no context ...")
}

fun View.setBackgroundCompat(@Nullable drawable: Drawable) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        this.background = drawable
    } else {
        this.setBackgroundDrawable(drawable)
    }
}

fun Toolbar.tintOverflowIcon(@ColorInt color: Int) {
    val overflowDrawable = overflowIcon
    if (overflowDrawable != null) {
        overflowIcon = overflowDrawable.tint(color)
    }
}

fun Toolbar.tintMenuItem(menu: Menu, activeColor: ActiveColor) {
    for (i in 0 until menu.size()) {
        val item = menu.getItem(i)
        if (item.icon != null) {
            item.icon = item.icon.tint(activeColor.toEnabledSl())
        }
        if (item.actionView is SearchView) {
            (item.actionView as? SearchView?)?.tint(activeColor)
        }
    }
}

fun Toolbar.tintCollapseIcon(colorStateList: ColorStateList) {
    try {
        val field = Toolbar::class.java.getDeclaredField("mCollapseIcon")
        field.isAccessible = true
        val collapseIcon = field.get(this) as? Drawable?
        if (collapseIcon != null) {
            field.set(this, collapseIcon.tint(colorStateList))
        }
        field.isAccessible = false
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Toolbar.tintNavIcon(colorStateList: ColorStateList) {
    try {
        val field = Toolbar::class.java.getDeclaredField("mNavButtonView")
        field.isAccessible = true
        val nav = field.get(this) as? ImageButton?
        nav?.setImageDrawable(nav.drawable?.tint(colorStateList))
        field.isAccessible = false
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun ActionMenuItemView.tintIcon(colorStateList: ColorStateList) {
    try {
        val field = ActionMenuItemView::class.java.getDeclaredField("mIcon")
        field.isAccessible = true
        val icon = field.get(this) as? Drawable?
        if (icon != null) {
            field.set(this, icon.tint(colorStateList))
        }
        field.isAccessible = false
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun CheckBox.tint(@ColorInt color: Int, isDark: Boolean) {
    val sl = ColorStateList(arrayOf(
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked)),
            intArrayOf(context.colorRes(if (isDark) R.color.md_control_disabled_dark else R.color.md_control_disabled_light), context.colorRes(if (isDark) R.color.md_control_normal_dark else R.color.md_control_normal_light), color))
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.buttonTintList = sl
    } else {
        @SuppressLint("PrivateResource")
        this.buttonDrawable = ContextCompat.getDrawable(context, R.drawable.abc_btn_check_material)?.tint(sl)
    }
}

fun ImageView.tint(@ColorInt color: Int) {
    this.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}


fun Switch.tint(@ColorInt color: Int, isDark: Boolean) {
    if (this.trackDrawable != null) {
        this.trackDrawable = TintUtils.tintSwitchDrawable(context, this.trackDrawable, color, false, false, isDark)
    }
    if (this.thumbDrawable != null) {
        this.thumbDrawable = TintUtils.tintSwitchDrawable(context, this.thumbDrawable, color, true, false, isDark)
    }
}


fun SwitchCompat.tint(@ColorInt color: Int, isDark: Boolean) {
    if (this.trackDrawable != null) {
        this.trackDrawable = TintUtils.tintSwitchDrawable(context, this.trackDrawable, color, false, true, isDark)
    }
    if (this.thumbDrawable != null) {
        this.thumbDrawable = TintUtils.tintSwitchDrawable(context, this.thumbDrawable, color, true, true, isDark)
    }
}

fun RadioButton.tint(@ColorInt color: Int, isDark: Boolean) {
    val sl = ColorStateList(
            arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked)
            ),
            intArrayOf(
                    // Radio button includes own alpha for disabled state
                    context.colorRes(if (isDark) R.color.md_control_disabled_dark else R.color.md_control_disabled_light).stripAlpha(),
                    context.colorRes(if (isDark) R.color.md_control_normal_dark else R.color.md_control_normal_light),
                    color
            )
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.buttonTintList = sl
    } else {
        @SuppressLint("PrivateResource")
        this.buttonDrawable = ContextCompat.getDrawable(context, R.drawable.abc_btn_radio_material)?.tint(sl)
    }
}

fun AbsSeekBar.tint(@ColorInt color: Int, isDark: Boolean) {
    val s1 = TintUtils.getDisabledColorStateList(color, context.colorRes(if (isDark) R.color.md_control_disabled_dark else R.color.md_control_disabled_light))
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.thumbTintList = s1
        this.progressTintList = s1
        this.secondaryProgressTintList = s1
    } else {
        this.progressDrawable = this.progressDrawable.tint(s1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.thumb = this.thumb.tint(s1)
        }
    }
}

fun ProgressBar.tint(@ColorInt color: Int) {
    this.tint(color, false)
}

fun ProgressBar.tint(@ColorInt color: Int, skipIndeterminate: Boolean) {
    val sl = ColorStateList.valueOf(color)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.progressTintList = sl
        this.secondaryProgressTintList = sl
        if (!skipIndeterminate) {
            this.indeterminateTintList = sl
        }
    } else {
        val mode = PorterDuff.Mode.SRC_IN
        if (!skipIndeterminate && this.indeterminateDrawable != null) {
            this.indeterminateDrawable.setColorFilter(color, mode)
        }
        if (this.progressDrawable != null) {
            this.progressDrawable.setColorFilter(color, mode)
        }
    }
}

fun EditText.tint(@ColorInt color: Int, isDark: Boolean) {
    val editTextColorStateList = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_pressed, -android.R.attr.state_focused), intArrayOf()),
            intArrayOf(context.colorRes(if (isDark) R.color.md_text_disabled_dark else R.color.md_text_disabled_light), context.colorRes(if (isDark) R.color.md_control_normal_dark else R.color.md_control_normal_light), color)
    )
    if (this is TintableBackgroundView) {
        ViewCompat.setBackgroundTintList(this, editTextColorStateList)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.backgroundTintList = editTextColorStateList
    }
    this.tintCursor(color)
}

fun EditText.tintCursor(@ColorInt color: Int) {
    try {
        val fCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        fCursorDrawableRes.isAccessible = true
        val mCursorDrawableRes = fCursorDrawableRes.getInt(this)
        fCursorDrawableRes.isAccessible = false
        val fEditor = TextView::class.java.getDeclaredField("mEditor")
        fEditor.isAccessible = true
        val editor = fEditor.get(this)
        fEditor.isAccessible = false
        val clazz = editor.javaClass
        val fCursorDrawable = clazz.getDeclaredField("mCursorDrawable")
        fCursorDrawable.isAccessible = true
        val drawables = arrayOfNulls<Drawable>(2)
        val tintDrawable = context.drawableRes(mCursorDrawableRes)?.tint(color)
        drawables[0] = tintDrawable
        drawables[1] = tintDrawable
        fCursorDrawable.set(editor, drawables)
        fCursorDrawable.isAccessible = false
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun TextInputLayout.setHintColor(@ColorInt hintColor: Int) {
    try {
        val defaultHintColorField = TextInputLayout::class.java.findField(
                "defaultHintTextColor", "mDefaultTextColor"
        )
        defaultHintColorField.isAccessible = true
        defaultHintColorField.set(this, ColorStateList.valueOf(hintColor))
        defaultHintColorField.isAccessible = false
        val updateLabelStateMethod = TextInputLayout::class.java.getDeclaredMethod(
                "updateLabelState", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType
        )
        updateLabelStateMethod.isAccessible = true
        updateLabelStateMethod.invoke(this, false, true)
        updateLabelStateMethod.isAccessible = false
    } catch (t: Throwable) {
        throw IllegalStateException(
                "Failed to set TextInputLayout hint (collapsed) color: " + t.localizedMessage, t
        )
    }
}

fun TextInputLayout.setAccentColor(@ColorInt accentColor: Int) {
    try {
        val focusedTextColor = TextInputLayout::class.java.findField(
                "focusedTextColor", "mFocusedTextColor"
        )
        focusedTextColor.isAccessible = true
        focusedTextColor.set(this, ColorStateList.valueOf(accentColor))
        focusedTextColor.isAccessible = false
        val updateLabelStateMethod = TextInputLayout::class.java.getDeclaredMethod(
                "updateLabelState", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType
        )
        updateLabelStateMethod.isAccessible = true
        updateLabelStateMethod.invoke(this, false, true)
        updateLabelStateMethod.isAccessible = false
    } catch (t: Throwable) {
        throw IllegalStateException(
                "Failed to set TextInputLayout accent (expanded) color: " + t.localizedMessage, t
        )
    }
}

fun TextInputLayout.setStrokeColor(@ColorInt accentColor: Int) {
    try {
        val disabledTextColor = TextInputLayout::class.java.findField("defaultStrokeColor")
        disabledTextColor.isAccessible = true
        disabledTextColor.set(this, accentColor)
        disabledTextColor.isAccessible = false
        val updateLabelStateMethod = TextInputLayout::class.java.getDeclaredMethod(
                "updateLabelState", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType
        )
        updateLabelStateMethod.isAccessible = true
        updateLabelStateMethod.invoke(this, false, true)
        updateLabelStateMethod.isAccessible = false
    } catch (t: Throwable) {
        throw IllegalStateException(
                "Failed to set TextInputLayout accent (expanded) color: " + t.localizedMessage, t
        )
    }
}

fun TextInputLayout.setStrokeColorHover(@ColorInt accentColor: Int) {
    try {
        val disabledTextColor = TextInputLayout::class.java.findField("hoveredStrokeColor")
        disabledTextColor.isAccessible = true
        disabledTextColor.set(this, accentColor)
        disabledTextColor.isAccessible = false
        val updateLabelStateMethod = TextInputLayout::class.java.getDeclaredMethod(
                "updateLabelState", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType
        )
        updateLabelStateMethod.isAccessible = true
        updateLabelStateMethod.invoke(this, false, true)
        updateLabelStateMethod.isAccessible = false
    } catch (t: Throwable) {
        throw IllegalStateException(
                "Failed to set TextInputLayout accent (expanded) color: " + t.localizedMessage, t
        )
    }
}

fun TextInputLayout.setStrokeColorFocused(@ColorInt accentColor: Int) {
    try {
        val disabledTextColor = TextInputLayout::class.java.findField("focusedStrokeColor")
        disabledTextColor.isAccessible = true
        disabledTextColor.set(this, accentColor)
        disabledTextColor.isAccessible = false
        val updateLabelStateMethod = TextInputLayout::class.java.getDeclaredMethod(
                "updateLabelState", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType
        )
        updateLabelStateMethod.isAccessible = true
        updateLabelStateMethod.invoke(this, false, true)
        updateLabelStateMethod.isAccessible = false
    } catch (t: Throwable) {
        throw IllegalStateException(
                "Failed to set TextInputLayout accent (expanded) color: " + t.localizedMessage, t
        )
    }
}

fun TextInputLayout.setDisabledColor(@ColorInt accentColor: Int) {
    try {
        val disabledTextColor = TextInputLayout::class.java.findField("disabledColor")
        disabledTextColor.isAccessible = true
        disabledTextColor.set(this, accentColor)
        disabledTextColor.isAccessible = false
        val updateLabelStateMethod = TextInputLayout::class.java.getDeclaredMethod(
                "updateLabelState", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType
        )
        updateLabelStateMethod.isAccessible = true
        updateLabelStateMethod.invoke(this, false, true)
        updateLabelStateMethod.isAccessible = false
    } catch (t: Throwable) {
        throw IllegalStateException(
                "Failed to set TextInputLayout accent (expanded) color: " + t.localizedMessage, t
        )
    }
}


fun SearchView.tint(color: ActiveColor) {
    try {
        val cls = javaClass
        val mSearchSrcTextViewField = cls.getDeclaredField("mSearchSrcTextView")
        mSearchSrcTextViewField.isAccessible = true
        val mSearchSrcTextView = mSearchSrcTextViewField.get(this) as EditText
        mSearchSrcTextView.setTextColor(color.active)
        mSearchSrcTextView.setHintTextColor(color.active.adjustAlpha(0.7f))
        mSearchSrcTextView.tintCursor(color.active)
        mSearchSrcTextViewField.isAccessible = false
        var field = cls.getDeclaredField("mSearchButton")
        TintUtils.tintImageViewDrawable(this, field, color)
        field = cls.getDeclaredField("mGoButton")
        TintUtils.tintImageViewDrawable(this, field, color)
        field = cls.getDeclaredField("mCloseButton")
        TintUtils.tintImageViewDrawable(this, field, color)
        field = cls.getDeclaredField("mVoiceButton")
        TintUtils.tintImageViewDrawable(this, field, color)

        field = cls.getDeclaredField("mSearchPlate")
        field.isAccessible = true
        (field.get(this) as View).tintAuto(color.active, true, !color.active.isColorLight())
        field.isAccessible = false
        field = cls.getDeclaredField("mSearchHintIcon")
        field.isAccessible = true
        field.set(this, (field.get(this) as? Drawable?)?.tint(color.toEnabledSl()))
        field.isAccessible = false
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@SuppressLint("PrivateResource")
fun View.tintAuto(@ColorInt color: Int, background: Boolean, isDark: Boolean) {
    var thisBg = background
    if (!thisBg) {
        when {
            this is RadioButton -> this.tint(color, isDark)
            this is SeekBar -> this.tint(color, isDark)
            this is ProgressBar -> this.tint(color)
            this is EditText -> this.tint(color, isDark)
            this is CheckBox -> this.tint(color, isDark)
            this is ImageView -> this.tint(color)
            this is Switch -> this.tint(color, isDark)
            this is SwitchCompat -> this.tint(color, isDark)
            else -> thisBg = true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !thisBg && this.background is RippleDrawable) {
            // Ripples for the above views (e.g. when you tap and hold a switch or checkbox)
            val rd = this.background as RippleDrawable
            val unchecked = this.activity().colorRes(if (isDark) R.color.ripple_material_dark else R.color.ripple_material_light)
            val checked = color.adjustAlpha(0.4f)
            val sl = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_activated, -android.R.attr.state_checked), intArrayOf(android.R.attr.state_activated), intArrayOf(android.R.attr.state_checked)), intArrayOf(unchecked, checked, checked))
            rd.setColor(sl)
        }
    }
    if (thisBg) {
        // Need to tint the background of a view
        if (this is FloatingActionButton || this is Button) {
            this.tintSelector(color, false, isDark)
        } else if (this.background != null) {
            var drawable: Drawable? = this.background
            if (drawable != null) {
                if (this is TextInputEditText) {
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                } else {
                    drawable = drawable.tint(color)
                    this.setBackgroundCompat(drawable)
                }
            }
        }
    }
}

fun View.tintSelector(@ColorInt color: Int, darker: Boolean, isDark: Boolean) {
    val isColorLight = color.isColorLight()
    val ctx = this.activity()
    val disabled = ctx.colorRes(if (isDark) R.color.md_button_disabled_dark else R.color.md_button_disabled_light)
    val pressed = color.shiftColor(if (darker) 0.9f else 1.1f)
    val activated = color.shiftColor(if (darker) 1.1f else 0.9f)
    val rippleColor = TintUtils.getDefaultRippleColor(ctx, isColorLight)
    val textColor = ctx.colorRes(if (isColorLight) R.color.md_primary_text_light else R.color.md_primary_text_dark)
    val sl: ColorStateList
    when {
        this is Button -> {
            sl = TintUtils.getDisabledColorStateList(color, disabled)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && this.getBackground() is RippleDrawable) {
                val rd = this.getBackground() as RippleDrawable
                rd.setColor(ColorStateList.valueOf(rippleColor))
            }
            // Disabled text oops state for buttons, may oops overridden later by ATE tags
            this.setTextColor(TintUtils.getDisabledColorStateList(textColor, ctx.colorRes(if (isDark) R.color.md_button_text_disabled_dark else R.color.md_button_text_disabled_light)))
        }
        this is FloatingActionButton -> {
            // FloatingActionButton doesn't support disabled state?
            sl = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_pressed), intArrayOf(android.R.attr.state_pressed)), intArrayOf(color, pressed))

            this.rippleColor = rippleColor
            this.backgroundTintList = sl
            if (this.drawable != null)
                this.setImageDrawable(this.drawable.tint(textColor))
            return
        }
        else -> sl = ColorStateList(
                arrayOf(
                        intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled),
                        intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed),
                        intArrayOf(android.R.attr.state_enabled, android.R.attr.state_activated),
                        intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked)
                ),
                intArrayOf(disabled, color, pressed, activated, activated)
        )
    }
    var drawable: Drawable? = this.background
    if (drawable != null) {
        drawable = drawable.tint(sl)
        this.setBackgroundCompat(drawable)
    }
    if (this is TextView && this !is Button) {
        this.setTextColor(TintUtils.getDisabledColorStateList(textColor, ctx.colorRes(if (isColorLight) R.color.md_text_disabled_light else R.color.md_text_disabled_dark)))
    }
}

fun TextInputLayout.tintHint(@ColorInt hintColor: Int) {
    try {
        val mDefaultTextColorField = TextInputLayout::class.java.getDeclaredField("mDefaultTextColor")
        mDefaultTextColorField.isAccessible = true
        mDefaultTextColorField.set(this, ColorStateList.valueOf(hintColor))
        mDefaultTextColorField.isAccessible = false
        val updateLabelStateMethod = TextInputLayout::class.java.getDeclaredMethod("updateLabelState", Boolean::class.java, Boolean::class.java)
        updateLabelStateMethod.isAccessible = true
        updateLabelStateMethod.invoke(this, false, true)
        updateLabelStateMethod.isAccessible = false
    } catch (t: Throwable) {
        throw  IllegalStateException("Failed to set TextInputLayout hint (collapsed) oops: ${t.localizedMessage}", t)
    }
}

fun TextInputLayout.tint(@ColorInt color: Int) {
    try {
        val mFocusedTextColorField = TextInputLayout::class.java.getDeclaredField("mFocusedTextColor")
        mFocusedTextColorField.isAccessible = true
        mFocusedTextColorField.set(this, ColorStateList.valueOf(color))
        mFocusedTextColorField.isAccessible = false
        val updateLabelStateMethod = TextInputLayout::class.java.getDeclaredMethod("updateLabelState", Boolean::class.java, Boolean::class.java)
        updateLabelStateMethod.isAccessible = true
        updateLabelStateMethod.invoke(this, false, true)
        updateLabelStateMethod.isAccessible = false
    } catch (t: Throwable) {
        throw  IllegalStateException("Failed to set TextInputLayout accent (expanded) oops: ${t.localizedMessage}", t)
    }
}

fun AppCompatCheckedTextView.tint(state: IsDarkColor) {
    var tint = state.color
    if (state.isDark) {
        tint = tint.shiftColor(1.1f)
    }
    tint = tint.adjustAlpha(.5f)
    val disabled = context.colorRes(if (state.isDark) R.color.md_switch_track_disabled_dark else R.color.md_switch_track_disabled_light)
    val normal = context.colorRes(if (state.isDark) R.color.md_switch_track_normal_dark else R.color.md_switch_track_normal_light).stripAlpha()
    val sl = ColorStateList(
            arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_activated, -android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_enabled, android.R.attr.state_activated),
                    intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked)
            ),
            intArrayOf(disabled, normal, tint, tint)
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        compoundDrawableTintList = sl
    } else {
        for (compoundDrawable in compoundDrawables) {
            compoundDrawable?.setColorFilter(state.color, PorterDuff.Mode.SRC_IN)
        }
    }
}

fun SwipeRefreshLayout.tintCircleBackground(@ColorInt color: Int) {
    try {
        val field = SwipeRefreshLayout::class.java.getDeclaredField("mCircleView")
        field.isAccessible = true
        (field.get(this) as? ImageView?)?.setBackgroundColor(color)
        field.isAccessible = false
    } catch (e: Exception) {
        loge(e) { "tint circle background error ..." }
    }
}