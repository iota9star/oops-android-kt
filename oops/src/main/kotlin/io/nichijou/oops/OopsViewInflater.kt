package io.nichijou.oops

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IdRes
import androidx.lifecycle.Observer
import com.google.android.material.internal.NavigationMenuItemView
import io.nichijou.oops.ext.*
import io.nichijou.oops.widget.*

class OopsViewInflater : ViewInflaterFactory {

  override fun createView(parent: View?, name: String, context: Context, tag: String, tagValue: String, attrs: AttributeSet, viewId: Int): View? {
    val view: View?
    when (name) {
      "androidx.appcompat.widget.DialogTitle" -> {
        view = DialogTitle(context, attrs)
      }
      "SearchView", "androidx.appcompat.widget.SearchView" -> {
        view = SearchView(context, attrs)
      }
      "com.google.android.material.snackbar.SnackbarContentLayout" -> {
        view = SnackbarContentLayout(context, attrs)
      }
      "TextView", "androidx.appcompat.widget.AppCompatTextView" -> {
        val parentName = if (parent == null) "" else parent::class.java.name
        view = when {
          parentName == "com.google.android.material.tabs.TabLayout.TabView"
            || parentName == "com.google.android.material.internal.NavigationMenuView"
            || (parentName.endsWith("SnackBarContentLayout") && context.attrValue(attrs, android.R.attr.id) == "@id/snackbar_text")
          -> {
            logd("this TextView is the child of $parentName, ignore it.")
            null
          }
          context.attrValue(attrs, android.R.attr.textAppearance) == "@android:style/TextAppearance.Toast" && context.attrValue(attrs, android.R.attr.id) == "@android:id/message" -> {
            logd("this TextView is the child of Toast, ignore it.")
            null
          }
          parent is android.widget.LinearLayout && context.attrValue(attrs, android.R.attr.id) == "@android:id/message" -> {
            logd("this is a dialog message TextView, ignore it.")
            null
          }
          else -> TextView(context, attrs)
        }
      }
      "CheckBox", "androidx.appcompat.widget.AppCompatCheckBox" -> {
        view = CheckBox(context, attrs)
      }
      "RadioButton", "androidx.appcompat.widget.AppCompatRadioButton" -> {
        view = RadioButton(context, attrs)
      }
      "CheckedTextView", "androidx.appcompat.widget.AppCompatCheckedTextView" -> {
        view = if (parent is NavigationMenuItemView) {
          logd("this CheckedTextView is the child of com.google.android.material.internal.NavigationMenuItemView, ignore it.")
          null
        } else {
          CheckedTextView(context, attrs)
        }
      }
      "ImageView", "androidx.appcompat.widget.AppCompatImageView" -> {
        view = when {
          isSearchIcon(viewId) -> {
            logd("this ImageView is the child of SearchView, ignore it.")
            null
          }
          parent != null && parent::class.java.name == "com.google.android.material.tabs.TabLayout.TabView" -> {
            TabImageView(context, attrs)
          }
          else -> ImageView(context, attrs)
        }
      }
      "ImageButton", "androidx.appcompat.widget.AppCompatImageButton" -> {
        view = ImageButton(context, attrs)
      }
      "Button", "androidx.appcompat.widget.AppCompatButton" -> {
        view = when {
          viewId == android.R.id.button1
            || viewId == android.R.id.button2
            || viewId == android.R.id.button3
          -> DialogButton(context, attrs)
          viewId == com.google.android.material.R.id.snackbar_action -> {
            logd("this Button is the child of SnackBarContentLayout, ignore it.")
            null
          }
          isBorderlessButton(context, attrs) -> BorderlessButton(context, attrs)
          else -> Button(context, attrs)
        }
      }
      "com.google.android.material.button.MaterialButton" -> {
        view = if (isBorderlessButton(context, attrs)) MaterialBorderlessButton(context, attrs)
        else MaterialButton(context, attrs)
      }
      "Toolbar", "androidx.appcompat.widget.Toolbar" -> {
        view = Toolbar(context, attrs)
      }
      "com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton" -> {
        view = ExtendedFloatingActionButton(context, attrs)
      }
      "com.google.android.material.floatingactionbutton.FloatingActionButton" -> {
        view = FloatingActionButton(context, attrs)
      }
      "com.google.android.material.bottomappbar.BottomAppBar" -> {
        view = BottomAppBar(context, attrs)
      }
      "androidx.drawerlayout.widget.DrawerLayout" -> {
        view = DrawerLayout(context, attrs)
      }
      "Switch" -> {
        view = Switch(context, attrs)
      }
      "androidx.appcompat.widget.SwitchCompat" -> {
        view = SwitchCompat(context, attrs)
      }
      "androidx.appcompat.view.menu.ActionMenuItemView" -> {
        view = ActionMenuItemView(context, attrs)
      }
      "com.google.android.material.tabs.TabLayout" -> {
        view = TabLayout(context, attrs)
      }
      "ProgressBar" -> {
        view = ProgressBar(context, attrs)
      }
      "androidx.core.widget.ContentLoadingProgressBar" -> {
        view = ContentLoadingProgressBar(context, attrs)
      }
      "EditText", "androidx.appcompat.widget.AppCompatEditText" -> {
        view = EditText(context, attrs)
      }
      "com.google.android.material.navigation.NavigationView" -> {
        view = NavigationView(context, attrs)
      }
      "androidx.cardview.widget.CardView" -> {
        view = CardView(context, attrs)
      }
      "com.google.android.material.card.MaterialCardView" -> {
        view = MaterialCardView(context, attrs)
      }
      "com.google.android.material.bottomnavigation.BottomNavigationView" -> {
        view = BottomNavigationView(context, attrs)
      }
      "com.google.android.material.appbar.CollapsingToolbarLayout" -> {
        view = CollapsingToolbarLayout(context, attrs)
      }
      "com.google.android.material.textfield.TextInputEditText" -> {
        view = TextInputEditText(context, attrs)
      }
      "com.google.android.material.textfield.TextInputLayout" -> {
        view = TextInputLayout(context, attrs)
      }
      "SeekBar", "androidx.appcompat.widget.AppCompatSeekBar" -> {
        view = SeekBar(context, attrs)
      }
      "RatingBar", "androidx.appcompat.widget.AppCompatRatingBar" -> {
        view = RatingBar(context, attrs)
      }
      "Spinner", "androidx.appcompat.widget.AppCompatSpinner" -> {
        view = Spinner(context, attrs)
      }
      "androidx.viewpager.widget.ViewPager" -> {
        view = ViewPager(context, attrs)
      }
      "androidx.core.widget.NestedScrollView" -> {
        view = NestedScrollView(context, attrs)
      }
      "androidx.recyclerview.widget.RecyclerView" -> {
        view = RecyclerView(context, attrs)
      }
      "ListView" -> {
        view = ListView(context, attrs)
      }
      "ScrollView" -> {
        view = ScrollView(context, attrs)
      }
      "androidx.swiperefreshlayout.widget.SwipeRefreshLayout" -> {
        view = SwipeRefreshLayout(context, attrs)
      }
      "com.google.android.material.appbar.AppBarLayout" -> {
        view = AppBarLayout(context, attrs)
      }
      "LinearLayout" -> {
        view = LinearLayout(context, attrs)
      }
      "RelativeLayout" -> {
        view = RelativeLayout(context, attrs)
      }
      "FrameLayout" -> {
        view = FrameLayout(context, attrs)
      }
      "androidx.constraintlayout.widget.ConstraintLayout" -> {
        view = ConstraintLayout(context, attrs)
      }
      "androidx.appcompat.widget.LinearLayoutCompat" -> {
        view = LinearLayoutCompat(context, attrs)
      }
      "AutoCompleteTextView", "androidx.appcompat.widget.AppCompatAutoCompleteTextView" -> {
        view = AutoCompleteTextView(context, attrs)
      }
      "MultiAutoCompleteTextView", "androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView" -> {
        view = MultiAutoCompleteTextView(context, attrs)
      }
      else -> view = null
    }
    return view?.apply {
      val backgroundAttrValue = this.context.attrValue(attrs, android.R.attr.background)
      this.activity().applyOopsThemeStore {
        live(backgroundAttrValue)
          ?.observe(this@apply as OopsLifecycleOwner, Observer {
            if (!needlessBackgroundColor(this@apply)) {
              this@apply.setBackgroundColor(it)
            }
          })
      }
    }
  }

  private fun needlessBackgroundColor(view: View): Boolean {
    return view is androidx.cardview.widget.CardView
      || view is androidx.appcompat.widget.Toolbar
      || view is com.google.android.material.tabs.TabLayout
      || view is com.google.android.material.bottomnavigation.BottomNavigationView
      || view is android.widget.Button
  }

  private fun isSearchIcon(@IdRes id: Int): Boolean {
    return id == androidx.appcompat.R.id.search_button
      || id == androidx.appcompat.R.id.search_mag_icon
      || id == androidx.appcompat.R.id.search_close_btn
      || id == androidx.appcompat.R.id.search_go_btn
      || id == androidx.appcompat.R.id.search_voice_btn
  }

  private fun isBorderlessButton(context: Context, attrs: AttributeSet?): Boolean {
    if (attrs == null) {
      return false
    }
    val b1 = context.resId(attrs, android.R.attr.background)
    val b2 = context.resId(attrs, R.attr.backgroundTint)
    if (b1 == -1 && b2 == -1) {
      return false
    }
    return try {
      val bn1 = context.resources.getResourceEntryName(b1)
      val bn2 = context.resources.getResourceEntryName(b2)
      bn1.endsWith("btn_borderless_material") || bn2.endsWith("mtrl_btn_transparent_bg_color")
    } catch (e: Exception) {
      false
    }
  }

}
