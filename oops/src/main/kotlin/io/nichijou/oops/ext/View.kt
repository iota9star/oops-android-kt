package io.nichijou.oops.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.TintableBackgroundView
import androidx.core.view.ViewCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.R
import io.nichijou.oops.color.IsDarkWithColor
import io.nichijou.oops.color.MaterialButtonColor
import io.nichijou.oops.color.PairColor
import io.nichijou.oops.utils.TintUtils
import io.nichijou.utils.*

fun View.resumeOopsLife() {
  if (this is OopsLifecycleOwner) {
    resumeOopsLife()
  }
}

fun View.pauseOopsLife() {
  if (this is OopsLifecycleOwner) {
    pauseOopsLife()
  }
}

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

fun ViewGroup.setMarginTopPlusStatusBarHeight() {
  val lp = this.layoutParams as ViewGroup.MarginLayoutParams
  lp.topMargin = context.activity().getStatusBarHeight() + lp.topMargin
  this.layoutParams = lp
}

fun View.setPaddingTopPlusStatusBarHeight() {
  setPadding(this.paddingLeft, this.paddingTop + context.activity().getStatusBarHeight(), this.paddingRight, this.paddingBottom)
}

fun Toolbar.tintOverflowIcon(@ColorInt color: Int) {
  val drawable = overflowIcon?.tint(color)
  if (drawable != null) {
    overflowIcon = drawable
  }
}

fun Toolbar.tintMenuItem(active: PairColor) {
  for (i in 0 until menu.size()) {
    val item = menu.getItem(i)
    if (item.icon != null) {
      item.icon = item.icon.tint(active.toEnabledSl())
    }
    if (item.actionView is SearchView) {
      (item.actionView as? SearchView?)?.tint(active)
    }
  }
}

fun Toolbar.tintNavIcon(colorStateList: ColorStateList?) {
  try {
    val field = Toolbar::class.java.getDeclaredField("mNavButtonView")
    field.isAccessible = true
    val nav = field.get(this) as? ImageButton?
    val drawable = nav?.drawable?.tint(colorStateList)
    if (nav != null && drawable != null) {
      nav.setImageDrawable(drawable)
    }
    field.isAccessible = false
  } catch (e: Exception) {
    e.printStackTrace()
  }
}

fun Toolbar.tintCollapseIcon(colorStateList: ColorStateList?) {
  try {
    val field = Toolbar::class.java.getDeclaredField("mCollapseButtonView")
    field.isAccessible = true
    val collapseButton = field.get(this) as? ImageButton?
    val drawable = collapseButton?.drawable
    if (collapseButton != null && drawable != null) {
      collapseIcon = drawable.tint(colorStateList)
    }
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

fun CheckBox.tint(color: IsDarkWithColor) {
  val sl = ColorStateList(arrayOf(
    intArrayOf(-android.R.attr.state_enabled),
    intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked),
    intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked)),
    intArrayOf(context.colorRes(if (color.isDark) R.color.md_control_disabled_dark else R.color.md_control_disabled_light), context.colorRes(if (color.isDark) R.color.md_control_normal_dark else R.color.md_control_normal_light), color.color))
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    this.buttonTintList = sl
  } else {
    @SuppressLint("PrivateResource")
    this.buttonDrawable = ContextCompat.getDrawable(context, R.drawable.abc_btn_check_material)
      ?.tint(sl)
  }
  this.oopsTintRippleBackground(color)
}

fun ImageView.tint(@ColorInt color: Int) {
  this.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}

fun Switch.tint(color: IsDarkWithColor) {
  if (this.trackDrawable != null) {
    this.trackDrawable = TintUtils.tintSwitchDrawable(context, this.trackDrawable, color.color, false, false, color.isDark)
  }
  if (this.thumbDrawable != null) {
    this.thumbDrawable = TintUtils.tintSwitchDrawable(context, this.thumbDrawable, color.color, true, false, color.isDark)
  }
  this.oopsTintRippleBackground(color)
}

fun SwitchCompat.tint(color: IsDarkWithColor) {
  if (this.trackDrawable != null) {
    this.trackDrawable = TintUtils.tintSwitchDrawable(context, this.trackDrawable, color.color, false, true, color.isDark)
  }
  if (this.thumbDrawable != null) {
    this.thumbDrawable = TintUtils.tintSwitchDrawable(context, this.thumbDrawable, color.color, true, true, color.isDark)
  }
  this.oopsTintRippleBackground(color)
}

fun RadioButton.tint(color: IsDarkWithColor) {
  val sl = ColorStateList(
    arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_checked), intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked)),
    intArrayOf(context.colorRes(if (color.isDark) R.color.md_control_disabled_dark else R.color.md_control_disabled_light).stripAlpha(), context.colorRes(if (color.isDark) R.color.md_control_normal_dark else R.color.md_control_normal_light), color.color)
  )
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    this.buttonTintList = sl
  } else {
    @SuppressLint("PrivateResource")
    this.buttonDrawable = ContextCompat.getDrawable(context, R.drawable.abc_btn_radio_material)
      ?.tint(sl)
  }
  this.oopsTintRippleBackground(color)
}

fun Spinner.tint(@ColorInt color: Int) {
  this.background?.apply {
    background = this.tint(color)
  }
}

fun AbsSeekBar.tint(color: IsDarkWithColor) {
  val s1 = TintUtils.getDisabledColorStateList(color.color, context.colorRes(if (color.isDark) R.color.md_control_disabled_dark else R.color.md_control_disabled_light))
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    this.thumbTintList = s1
    this.progressTintList = s1
    this.secondaryProgressTintList = s1
  } else {
    this.progressDrawable = this.progressDrawable.tint(s1)
    this.thumb = this.thumb?.tint(s1)
  }
  this.oopsTintRippleBackground(color)
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

fun EditText.tint(color: IsDarkWithColor) {
  val editTextColorStateList = ColorStateList(
    arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_pressed, -android.R.attr.state_focused), intArrayOf()),
    intArrayOf(context.colorRes(if (color.isDark) R.color.md_text_disabled_dark else R.color.md_text_disabled_light), context.colorRes(if (color.isDark) R.color.md_control_normal_dark else R.color.md_control_normal_light), color.color)
  )
  if (this is TintableBackgroundView) {
    ViewCompat.setBackgroundTintList(this, editTextColorStateList)
  } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    this.backgroundTintList = editTextColorStateList
  }
  this.oopsTintRippleBackground(color)
}

fun EditText.tintCursor(@ColorInt color: Int) {
  try {
    val fCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
    fCursorDrawableRes.isAccessible = true
    val mCursorDrawableRes = fCursorDrawableRes.getInt(this)
    val fEditor = TextView::class.java.getDeclaredField("mEditor")
    fEditor.isAccessible = true
    val editor = fEditor.get(this)
    val fCursorDrawable = editor::class.java.getDeclaredField("mCursorDrawable")
    fCursorDrawable.isAccessible = true
    val drawables = arrayOf(
      ContextCompat.getDrawable(context, mCursorDrawableRes),
      ContextCompat.getDrawable(context, mCursorDrawableRes)
    )
    drawables[0]?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    drawables[1]?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    fCursorDrawable.set(editor, drawables)
  } catch (_: Exception) {
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
    throw IllegalStateException("Failed to set TextInputLayout accent (expanded) color: " + t.localizedMessage, t)
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

fun SearchView.tint(color: PairColor) {
  try {
    val cls = javaClass
    val mSearchSrcTextViewField = cls.getDeclaredField("mSearchSrcTextView")
    mSearchSrcTextViewField.isAccessible = true
    val mSearchSrcTextView = mSearchSrcTextViewField.get(this) as AppCompatAutoCompleteTextView
    mSearchSrcTextView.setTextColor(color.first)
    mSearchSrcTextView.setHintTextColor(color.first.adjustAlpha(0.7f))
    mSearchSrcTextView.tintCursor(color.first)
    mSearchSrcTextViewField.isAccessible = false
    var field = cls.getDeclaredField("mSearchButton")
    TintUtils.tintImageViewDrawable(this, field, color)
    field = cls.getDeclaredField("mGoButton")
    TintUtils.tintImageViewDrawable(this, field, color)
    field = cls.getDeclaredField("mCloseButton")
    TintUtils.tintImageViewDrawable(this, field, color)
    field = cls.getDeclaredField("mVoiceButton")
    TintUtils.tintImageViewDrawable(this, field, color)
    field = cls.getDeclaredField("mCollapsedIcon")
    TintUtils.tintImageViewDrawable(this, field, color)

    field = cls.getDeclaredField("mSearchPlate")
    field.isAccessible = true
    (field.get(this) as View).apply {
      this.background?.apply {
        background = this.tint(color.first)
      }
    }
    field.isAccessible = false
    field = cls.getDeclaredField("mSearchHintIcon")
    field.isAccessible = true
    field.set(this, (field.get(this) as? Drawable?)?.tint(color.toEnabledSl()))
    field.isAccessible = false
  } catch (e: Exception) {
    e.printStackTrace()
  }
}

fun FloatingActionButton.tint(@ColorInt color: Int) {
  val isLight = color.isColorLight()
  val pressed = color.lighten(if (!isLight) 0.9f else 1.1f)
  val sl = ColorStateList(
    arrayOf(intArrayOf(-android.R.attr.state_pressed), intArrayOf(android.R.attr.state_pressed)),
    intArrayOf(color, pressed)
  )
  val textColor = context.colorRes(if (isLight) R.color.md_primary_text_light else R.color.md_primary_text_dark)
  this.rippleColor = defaultRippleColor(context, isLight)
  this.backgroundTintList = sl
  if (this.drawable != null) {
    setImageDrawable(drawable.tint(textColor))
  }
}

fun View.oopsTintRippleBackground(color: IsDarkWithColor) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && this.background is RippleDrawable) {
    // Ripples for the above views (e.g. when you tap and hold a switch or checkbox)
    val rd = this.background as RippleDrawable
    val unchecked = context.colorRes(if (color.isDark) com.google.android.material.R.color.ripple_material_dark else com.google.android.material.R.color.ripple_material_light)
    val checked = color.color.adjustAlpha(0.4f)
    val sl = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_activated, -android.R.attr.state_checked), intArrayOf(android.R.attr.state_activated), intArrayOf(android.R.attr.state_checked)), intArrayOf(unchecked, checked, checked))
    rd.setColor(sl)
  }
}

fun MaterialButton.tint(color: MaterialButtonColor) {
  val useDarkTheme = color.isDark
  val bgColor = color.bgColor ?: Oops.immed().colorAccent
  val darker = bgColor.isColorDark()
  val disabled = if (color.bgColor == null) context.colorRes(if (useDarkTheme) R.color.md_button_disabled_dark else R.color.md_button_disabled_light) else bgColor.adjustAlpha(.64f)
  val pressed = bgColor.lighten(if (darker) 0.9f else 1.1f)
  val activated = bgColor.lighten(if (darker) 1.1f else 0.9f)
  val rippleColor = color.rippleColor?.adjustAlpha(.87f) ?: defaultRippleColor(context, darker)
  val textColor = color.textColor ?: context.colorRes(
    if (darker) R.color.md_primary_text_light
    else R.color.md_primary_text_dark
  )
  val sl = ColorStateList(
    arrayOf(
      intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled),
      intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed),
      intArrayOf(android.R.attr.state_enabled, android.R.attr.state_activated),
      intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked)
    ), intArrayOf(disabled, bgColor, pressed, activated, activated))
  val disabledTextColor = if (color.textColor == null) context.colorRes(if (useDarkTheme) R.color.md_button_text_disabled_dark else R.color.md_button_text_disabled_light) else color.textColor.adjustAlpha(.64f)
  val dsl = disabledColorStateList(textColor, disabledTextColor)
  this.setTextColor(dsl)
  this.icon = this.icon?.tint(dsl)
  this.backgroundTintList = sl
  setRippleColor(ColorStateList.valueOf(rippleColor))
  if (color.strokeColor != null) strokeColor = ColorStateList.valueOf(color.strokeColor)
}

fun Button.tint(@ColorInt color: Int, darker: Boolean, useDarkTheme: Boolean) {
  val disabled = context.colorRes(if (useDarkTheme) R.color.md_button_disabled_dark else R.color.md_button_disabled_light)
  val pressed = color.lighten(if (darker) 0.9f else 1.1f)
  val activated = color.lighten(if (darker) 1.1f else 0.9f)
  val rippleColor = defaultRippleColor(context, darker)
  val textColor = context.colorRes(
    if (darker) R.color.md_primary_text_light
    else R.color.md_primary_text_dark
  )
  val sl = ColorStateList(
    arrayOf(
      intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled),
      intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed),
      intArrayOf(android.R.attr.state_enabled, android.R.attr.state_activated),
      intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked)
    ), intArrayOf(disabled, color, pressed, activated, activated))
  val dsl = disabledColorStateList(textColor, context.colorRes(if (useDarkTheme) R.color.md_button_text_disabled_dark else R.color.md_button_text_disabled_light))
  this.setTextColor(dsl)
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && this.background is RippleDrawable) {
    val rd = this.background as RippleDrawable
    rd.setColor(ColorStateList.valueOf(rippleColor))
  }
  this.background?.apply {
    background = this.tint(sl)
  }
}

fun Button.tintBorderless(@ColorInt color: Int) {
  val rippleColor = color.adjustAlpha(.56f)
  val textColorSl = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf(-android.R.attr.state_enabled)), intArrayOf(color, color.adjustAlpha(.56f)))
  this.setTextColor(textColorSl)
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && this.background is RippleDrawable) {
    val rd = this.background as RippleDrawable
    rd.setColor(ColorStateList.valueOf(rippleColor))
    background = rd
  }
}

@SuppressLint("PrivateResource")
@ColorInt
private fun defaultRippleColor(context: Context, useDarkRipple: Boolean): Int {
  return context.colorRes(if (useDarkRipple) R.color.ripple_material_light else R.color.ripple_material_dark
  )
}

private fun disabledColorStateList(@ColorInt normal: Int, @ColorInt disabled: Int): ColorStateList {
  return ColorStateList(
    arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled)),
    intArrayOf(disabled, normal))
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

fun AppCompatCheckedTextView.tint(color: IsDarkWithColor) {
  var tint = color.color
  if (color.isDark) {
    tint = tint.lighten(1.1f)
  }
  tint = tint.adjustAlpha(.5f)
  val disabled = context.colorRes(if (color.isDark) R.color.md_switch_track_disabled_dark else R.color.md_switch_track_disabled_light)
  val normal = context.colorRes(if (color.isDark) R.color.md_switch_track_normal_dark else R.color.md_switch_track_normal_light)
    .stripAlpha()
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
      compoundDrawable?.setColorFilter(color.color, PorterDuff.Mode.SRC_IN)
    }
  }
  this.oopsTintRippleBackground(color)
}

fun SwipeRefreshLayout.tintCircleBackground(@ColorInt color: Int) {
  try {
    val field = SwipeRefreshLayout::class.java.getDeclaredField("mCircleView")
    field.isAccessible = true
    (field.get(this) as? ImageView?)?.setBackgroundColor(color)
    field.isAccessible = false
  } catch (e: Exception) {
    loge("tint SwipeRefreshLayout circle background error ...", e)
  }
}
