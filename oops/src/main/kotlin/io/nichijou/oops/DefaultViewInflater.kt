package io.nichijou.oops


import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.*

class DefaultViewInflater : ViewInflaterFactory {
  @SuppressLint("RestrictedApi")
  override fun createView(parent: View?, name: String, context: Context, tag: String, tagValue: String, attrs: AttributeSet, viewId: Int): View? {
    return when (name) {
      "TextView", "androidx.appcompat.widget.AppCompatTextView" -> AppCompatTextView(context, attrs)
      "ImageView", "androidx.appcompat.widget.AppCompatImageView" -> AppCompatImageView(context, attrs)
      "Button", "androidx.appcompat.widget.AppCompatButton" -> AppCompatButton(context, attrs)
      "EditText", "androidx.appcompat.widget.AppCompatEditText" -> AppCompatEditText(context, attrs)
      "Spinner", "androidx.appcompat.widget.AppCompatSpinner" -> AppCompatSpinner(context, attrs)
      "ImageButton", "androidx.appcompat.widget.AppCompatImageButton" -> AppCompatImageButton(context, attrs)
      "CheckBox", "androidx.appcompat.widget.AppCompatCheckBox" -> AppCompatCheckBox(context, attrs)
      "RadioButton", "androidx.appcompat.widget.AppCompatRadioButton" -> AppCompatRadioButton(context, attrs)
      "CheckedTextView", "androidx.appcompat.widget.AppCompatCheckedTextView" -> AppCompatCheckedTextView(context, attrs)
      "AutoCompleteTextView", "androidx.appcompat.widget.AppCompatAutoCompleteTextView" -> AppCompatAutoCompleteTextView(context, attrs)
      "MultiAutoCompleteTextView", "androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView" -> AppCompatMultiAutoCompleteTextView(context, attrs)
      "RatingBar", "androidx.appcompat.widget.AppCompatRatingBar" -> AppCompatRatingBar(context, attrs)
      "SeekBar", "androidx.appcompat.widget.AppCompatSeekBar" -> AppCompatSeekBar(context, attrs)
      "ToggleButton", "androidx.appcompat.widget.AppCompatToggleButton" -> AppCompatToggleButton(context, attrs)
      "androidx.appcompat.widget.SearchView" -> SearchView(context, attrs)
      "androidx.appcompat.widget.Toolbar" -> Toolbar(context, attrs)
      "androidx.appcompat.widget.SwitchCompat" -> SwitchCompat(context, attrs)
      "androidx.appcompat.widget.LinearLayoutCompat" -> LinearLayoutCompat(context, attrs)
      "androidx.appcompat.widget.ActionMenuView" -> ActionMenuView(context, attrs)
      "androidx.appcompat.widget.ButtonBarLayout" -> ButtonBarLayout(context, attrs)
      "androidx.appcompat.widget.ContentFrameLayout" -> ContentFrameLayout(context, attrs)
      "androidx.appcompat.widget.FitWindowsFrameLayout" -> FitWindowsFrameLayout(context, attrs)
      "androidx.appcompat.widget.FitWindowsLinearLayout" -> FitWindowsLinearLayout(context, attrs)
      "androidx.appcompat.widget.AlertDialogLayout" -> AlertDialogLayout(context, attrs)
      "androidx.appcompat.widget.ActivityChooserView" -> ActivityChooserView(context, attrs)
      "androidx.appcompat.widget.ActionBarOverlayLayout" -> ActionBarOverlayLayout(context, attrs)
      "androidx.appcompat.widget.ActionBarContextView" -> ActionBarContextView(context, attrs)
      "androidx.appcompat.widget.ActionBarContainer" -> ActionBarContainer(context, attrs)
      else -> null
    }
  }
}

