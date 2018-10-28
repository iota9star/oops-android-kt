package io.nichijou.oops

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.AttributeSet
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewParent
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.*
import androidx.cardview.widget.CardView
import androidx.collection.ArrayMap
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import io.nichijou.oops.ext.attrName
import io.nichijou.oops.ext.logi
import io.nichijou.oops.ext.resId
import io.nichijou.oops.widget.*
import org.xmlpull.v1.XmlPullParser
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

@SuppressLint("RestrictedApi")
class OopsFactory2Impl(private val activity: AppCompatActivity, private val factory: OopsLayoutInflaterFactory?) : LayoutInflater.Factory2 {

    private fun shouldInheritContext(context: Context, parent: ViewParent?): Boolean {
        var thisParent: ViewParent? = parent ?: return false
        if (context is AppCompatActivity) {
            val windowDecor = context.window.decorView
            while (true) {
                if (thisParent == null) {
                    return true
                } else if (thisParent === windowDecor
                        || thisParent !is View
                        || ViewCompat.isAttachedToWindow((thisParent as View))) {
                    return false
                }
                thisParent = (thisParent as ViewParent).parent
            }
        } else return false
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet?): View? {
        var inheritContext = false
        val isPreLollipop = Build.VERSION.SDK_INT < 21
        if (isPreLollipop) {
            inheritContext = if (attrs is XmlPullParser)
                (attrs as XmlPullParser).depth > 1
            else
                shouldInheritContext(context, parent as ViewParent)
        }
        return createView(parent, name, context, attrs, inheritContext, isPreLollipop, true, VectorEnabledTintResources.shouldBeUsed())
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet?): View? {
        return onCreateView(null, name, context, attrs)
    }

    private val mConstructorArgs = arrayOfNulls<Any>(2)

    private fun createView(parent: View?, name: String, context: Context, attrs: AttributeSet?, inheritContext: Boolean, readAndroidTheme: Boolean, readAppTheme: Boolean, wrapContext: Boolean): View? {
        var ctx = context
        val originalContext = ctx
        if (inheritContext && parent != null) {
            ctx = parent.context
        }
        if (readAndroidTheme || readAppTheme) {
            ctx = themifyContext(ctx, attrs, readAndroidTheme, readAppTheme)
        }
        if (wrapContext) {
            ctx = TintContextWrapper.wrap(ctx)
        }
        var view = createOopsView(parent, name, ctx, attrs)
        if (view != null && ((view.tag != null && view.tag == context.getString(R.string.oops_ignore_view)) || view.getTag(R.string.oops_ignore_view) != null)) {
            view = createDefaultView(name, ctx, attrs)
        }
        if (view == null) {
            try {
                view = activity.onCreateView(parent, name, ctx, attrs)
                if (view == null) {
                    view = activity.onCreateView(name, ctx, attrs)
                }
            } catch (e: Throwable) {
                throw IllegalStateException("Unable to inflate $name by ${activity.javaClass.canonicalName}", e)
            }
        }
        if (view == null && originalContext !== ctx) {
            view = createViewFromTag(ctx, name, attrs)
        }
        if (view != null) {
            checkOnClickListener(view, attrs)
        }
        return view
    }

    private fun createOopsView(parent: View?, name: String, context: Context, attrs: AttributeSet?): View? {
        val view: View?
        val viewId = context.resId(attrs, android.R.attr.id)
        when (name) {
            "SearchView", "androidx.appcompat.widget.SearchView" -> {
                view = OopsSearchView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.snackbar.SnackbarContentLayout" -> {
                view = OopsSnackBarContentLayout(context, attrs)
                verifyNotNull(view, name, false)
            }
            "TextView", "androidx.appcompat.widget.AppCompatTextView" -> {
                if (parent != null) {
                    val parentName = parent::class.java.canonicalName
                    if (parentName == "com.google.android.material.tabs.TabLayout.TabView" || parentName == "com.google.android.material.internal.NavigationMenuView") {
                        logi { "this TextView is the child of $parentName, we ignore it." }
                        return null
                    }
                }
                view = if (parent != null && parent::class.java.toString().endsWith("SnackBarContentLayout") && context.attrName(attrs, android.R.attr.id) == "@id/snackbar_text") {
                    OopsSnackBarTextView(context, attrs)
                } else if (context.attrName(attrs, android.R.attr.textAppearance) == "@android:style/TextAppearance.Toast" && context.attrName(attrs, android.R.attr.id) == "@android:id/message") {
                    OopsToastTextView(context, attrs)
                } else if (parent is LinearLayout && context.attrName(attrs, android.R.attr.id) == "@android:id/message") {
                    null
                } else {
                    OopsTextView(context, attrs)
                }
                if (view == null) {
                    logi { "this is a dialog message TextView, we ignore it." }
                } else {
                    verifyNotNull(view, name, false)
                }
            }
            "CheckBox", "androidx.appcompat.widget.AppCompatCheckBox" -> {
                view = OopsCheckBox(context, attrs)
                verifyNotNull(view, name, false)
            }
            "RadioButton", "androidx.appcompat.widget.AppCompatRadioButton" -> {
                view = OopsRadioButton(context, attrs)
                verifyNotNull(view, name, false)
            }
            "CheckedTextView", "androidx.appcompat.widget.AppCompatCheckedTextView" -> {
                if (parent != null && parent::class.java.canonicalName == "com.google.android.material.internal.NavigationMenuItemView") {
                    logi { "this CheckedTextView is the child of com.google.android.material.internal.NavigationMenuItemView, we ignore it." }
                    return null
                }
                view = OopsCheckedTextView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "ImageView", "androidx.appcompat.widget.AppCompatImageView" -> {
                if (isSearchIcon(context.resId(attrs, android.R.attr.id))) {
                    logi { "this ImageView is the child of SearchView, we ignore it." }
                    return null
                }
                view = if (parent != null && parent.javaClass.canonicalName == "com.google.android.material.tabs.TabLayout.TabView") OopsTabImageView(context, attrs)
                else OopsImageView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "ImageButton", "androidx.appcompat.widget.AppCompatImageButton" -> {
                view = OopsImageButton(context, attrs)
                verifyNotNull(view, name, false)
            }
            "Button", "androidx.appcompat.widget.AppCompatButton" -> {
                view = when {
                    viewId == android.R.id.button1 || viewId == android.R.id.button2 || viewId == android.R.id.button3 -> OopsDialogButton(context, attrs)
                    viewId == com.google.android.material.R.id.snackbar_action -> OopsSnackBarButton(context, attrs)
                    isBorderlessButton(context, attrs) -> OopsBorderlessButton(context, attrs)
                    else -> OopsButton(context, attrs)
                }
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.button.MaterialButton" -> {
                view = if (isBorderlessButton(context, attrs)) OopsMaterialBorderlessButton(context, attrs)
                else OopsMaterialButton(context, attrs)
                verifyNotNull(view, name, false)
            }
            "Toolbar", "androidx.appcompat.widget.Toolbar" -> {
                view = OopsToolbar(context, attrs)
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.floatingactionbutton.FloatingActionButton" -> {
                view = OopsFloatingActionButton(context, attrs)
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.bottomappbar.BottomAppBar" -> {
                view = OopsBottomAppBar(context, attrs)
                verifyNotNull(view, name, false)
            }
            "androidx.drawerlayout.widget.DrawerLayout" -> {
                view = OopsDrawerLayout(context, attrs)
                verifyNotNull(view, name, false)
            }
            "Switch", "androidx.appcompat.widget.SwitchCompat" -> {
                view = OopsSwitch(context, attrs)
                verifyNotNull(view, name, false)
            }
            "androidx.appcompat.view.menu.ActionMenuItemView" -> {
                view = OopsActionMenuItemView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.tabs.TabLayout" -> {
                view = OopsTabLayout(context, attrs)
                verifyNotNull(view, name, false)
            }
            "ProgressBar" -> {
                view = OopsProgressBar(context, attrs)
                verifyNotNull(view, name, false)
            }
            "EditText", "androidx.appcompat.widget.AppCompatEditText" -> {
                view = OopsEditText(context, attrs)
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.navigation.NavigationView" -> {
                view = OopsNavigationView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "androidx.cardview.widget.CardView" -> {
                view = OopsCardView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.card.MaterialCardView" -> {
                view = OopsMaterialCardView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.bottomnavigation.BottomNavigationView" -> {
                view = OopsBottomNavigationView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.appbar.CollapsingToolbarLayout" -> {
                view = OopsCollapsingToolbarLayout(context, attrs)
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.textfield.TextInputEditText" -> {
                view = OopsTextInputEditText(context, attrs)
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.textfield.TextInputLayout" -> {
                view = OopsTextInputLayout(context, attrs)
                verifyNotNull(view, name, false)
            }
            "SeekBar", "androidx.appcompat.widget.AppCompatSeekBar" -> {
                view = OopsSeekBar(context, attrs)
                verifyNotNull(view, name, false)
            }
            "RatingBar", "androidx.appcompat.widget.AppCompatRatingBar" -> {
                view = OopsRatingBar(context, attrs)
                verifyNotNull(view, name, false)
            }
            "Spinner", "androidx.appcompat.widget.AppCompatSpinner" -> {
                view = OopsSeekBar(context, attrs)
                verifyNotNull(view, name, false)
            }
            "androidx.viewpager.widget.ViewPager" -> {
                view = OopsViewPager(context, attrs)
                verifyNotNull(view, name, false)
            }
            "androidx.core.widget.NestedScrollView" -> {
                view = OopsNestedScrollView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "androidx.recyclerview.widget.RecyclerView" -> {
                view = OopsRecyclerView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "ListView" -> {
                view = OopsListView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "ScrollView" -> {
                view = OopsScrollView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "androidx.swiperefreshlayout.widget.SwipeRefreshLayout" -> {
                view = OopsSwipeRefreshLayout(context, attrs)
                verifyNotNull(view, name, false)
            }
            "LinearLayout" -> {
                view = OopsLinearLayout(context, attrs)
                verifyNotNull(view, name, false)
            }
            "RelativeLayout" -> {
                view = OopsRelativeLayout(context, attrs)
                verifyNotNull(view, name, false)
            }
            "FrameLayout" -> {
                view = OopsFrameLayout(context, attrs)
                verifyNotNull(view, name, false)
            }
            "androidx.constraintlayout.widget.ConstraintLayout" -> {
                view = OopsConstraintLayout(context, attrs)
                verifyNotNull(view, name, false)
            }
            "androidx.appcompat.widget.LinearLayoutCompat" -> {
                view = OopsLinearLayoutCompat(context, attrs)
                verifyNotNull(view, name, false)
            }
            else -> {
                view = factory?.onCreateView(parent, name, context, attrs, viewId)
            }
        }
        return view?.apply {
            if (this !is OopsViewLifeAndLive) return@apply
            val backgroundAttrName = this.context.attrName(attrs, android.R.attr.background)
            (this as OopsViewLifeAndLive).getOopsViewModel().live(backgroundAttrName)?.observe(this as OopsViewLifeAndLive, Observer {
                if (!needlessBackgroundColor(this)) {
                    this.setBackgroundColor(it)
                }
            })
        }
    }

    private fun needlessBackgroundColor(view: View): Boolean {
        return view is CardView
                || view is Toolbar
                || view is TabLayout
                || view is BottomNavigationView
                || view is Button
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

    private fun createDefaultView(name: String, context: Context, attrs: AttributeSet?): View? {
        val view: View?
        when (name) {
            "TextView" -> {
                view = AppCompatTextView(context, attrs)
                verifyNotNull(view, name, true)
            }
            "ImageView" -> {
                view = AppCompatImageView(context, attrs)
                verifyNotNull(view, name, true)
            }
            "Button" -> {
                view = AppCompatButton(context, attrs)
                verifyNotNull(view, name, true)
            }
            "EditText" -> {
                view = AppCompatEditText(context, attrs)
                verifyNotNull(view, name, true)
            }
            "Spinner" -> {
                view = AppCompatSpinner(context, attrs)
                verifyNotNull(view, name, true)
            }
            "ImageButton" -> {
                view = AppCompatImageButton(context, attrs)
                verifyNotNull(view, name, true)
            }
            "CheckBox" -> {
                view = AppCompatCheckBox(context, attrs)
                verifyNotNull(view, name, true)
            }
            "RadioButton" -> {
                view = AppCompatRadioButton(context, attrs)
                verifyNotNull(view, name, true)
            }
            "CheckedTextView" -> {
                view = AppCompatCheckedTextView(context, attrs)
                verifyNotNull(view, name, true)
            }
            "AutoCompleteTextView" -> {
                view = AppCompatAutoCompleteTextView(context, attrs)
                verifyNotNull(view, name, true)
            }
            "MultiAutoCompleteTextView" -> {
                view = AppCompatMultiAutoCompleteTextView(context, attrs)
                verifyNotNull(view, name, true)
            }
            "RatingBar" -> {
                view = AppCompatRatingBar(context, attrs)
                verifyNotNull(view, name, true)
            }
            "SeekBar" -> {
                view = AppCompatSeekBar(context, attrs)
                verifyNotNull(view, name, true)
            }
            else ->
                view = null
        }
        return view
    }

    private fun verifyNotNull(view: View?, name: String, ignore: Boolean) {
        if (view == null) {
            throw IllegalStateException("${this.javaClass.name} asked to inflate view for <$name>, but returned null")
        }
        if (BuildConfig.DEBUG) {
            if (ignore) {
                logi { "ignore $name & inflate view : $name to ${view::class.java.simpleName}" }
            } else {
                logi { "inflate view : $name to ${view::class.java.simpleName}" }
            }
        }
    }

    private fun createViewFromTag(context: Context, name: String, attrs: AttributeSet?): View? {
        var thisName = name
        if (thisName == "view" && attrs != null) {
            thisName = attrs.getAttributeValue(null, "class")
        }
        try {
            mConstructorArgs[0] = context
            mConstructorArgs[1] = attrs
            if (-1 == thisName.indexOf('.')) {
                for (i in sClassPrefixList.indices) {
                    val view = createViewByPrefix(context, thisName, sClassPrefixList[i])
                    if (view != null) {
                        return view
                    }
                }
                return null
            } else {
                return createViewByPrefix(context, thisName, null)
            }
        } catch (e: Exception) {
            return null
        } finally {
            mConstructorArgs[0] = null
            mConstructorArgs[1] = null
        }
    }

    private fun checkOnClickListener(view: View, attrs: AttributeSet?) {
        val context = view.context
        if (context !is ContextWrapper && !ViewCompat.hasOnClickListeners(view)) {
            return
        }

        val a = context.obtainStyledAttributes(attrs, sOnClickAttrs)
        val handlerName = a.getString(0)
        if (handlerName != null) {
            view.setOnClickListener(DeclaredOnClickListener(view, handlerName))
        }
        a.recycle()
    }

    @Throws(ClassNotFoundException::class, InflateException::class)
    private fun createViewByPrefix(context: Context, name: String, prefix: String?): View? {
        var constructor: Constructor<out View>? = sConstructorMap[name]
        return try {
            if (constructor == null) {
                val clazz = context.classLoader.loadClass(
                        if (prefix != null) prefix + name else name).asSubclass(View::class.java)

                constructor = clazz.getConstructor(*sConstructorSignature)
                sConstructorMap[name] = constructor
            }
            constructor!!.isAccessible = true
            constructor.newInstance(*mConstructorArgs)
        } catch (e: Exception) {
            null
        }
    }

    private class DeclaredOnClickListener(private val mHostView: View, private val mMethodName: String) : View.OnClickListener {
        private var mResolvedMethod: Method? = null
        private var mResolvedContext: Context? = null
        override fun onClick(v: View) {
            if (mResolvedMethod == null) {
                resolveMethod(mHostView.context, mMethodName)
            }
            try {
                mResolvedMethod!!.invoke(mResolvedContext, v)
            } catch (e: IllegalAccessException) {
                throw IllegalStateException(
                        "Could not execute non-public method for android:onClick", e)
            } catch (e: InvocationTargetException) {
                throw IllegalStateException(
                        "Could not execute method for android:onClick", e)
            }
        }

        private fun resolveMethod(context: Context?, name: String) {
            var thisCtx = context
            while (thisCtx != null) {
                try {
                    if (!thisCtx.isRestricted) {
                        val method = thisCtx.javaClass.getMethod(mMethodName, View::class.java)
                        mResolvedMethod = method
                        mResolvedContext = thisCtx
                        return
                    }
                } catch (e: NoSuchMethodException) {
                }

                thisCtx = if (thisCtx is ContextWrapper) {
                    thisCtx.baseContext
                } else {
                    null
                }
            }

            val id = mHostView.id
            val idText = if (id == View.NO_ID) "" else " with id '" + mHostView.context.resources.getResourceEntryName(id) + "'"
            throw IllegalStateException(("Could not find method " + mMethodName
                    + "(View) in a parent or ancestor Context for android:onClick "
                    + "attribute defined on view " + mHostView.javaClass + idText))
        }
    }

    companion object {

        private val sConstructorSignature = arrayOf(Context::class.java, AttributeSet::class.java)

        private val sOnClickAttrs = intArrayOf(android.R.attr.onClick)

        private val sClassPrefixList = arrayOf("android.widget.", "android.view.", "android.webkit.")

        private val sConstructorMap = ArrayMap<String, Constructor<out View>>()

        private fun themifyContext(context: Context, attrs: AttributeSet?, useAndroidTheme: Boolean, useAppTheme: Boolean): Context {
            var thisCtx = context
            val a = thisCtx.obtainStyledAttributes(attrs, androidx.appcompat.R.styleable.View, 0, 0)
            var themeId = 0
            if (useAndroidTheme) {
                themeId = a.getResourceId(androidx.appcompat.R.styleable.View_android_theme, 0)
            }
            if (useAppTheme && themeId == 0) {
                themeId = a.getResourceId(androidx.appcompat.R.styleable.View_theme, 0)
                if (themeId != 0) {
                    logi { "app:theme is now deprecated. Please move to using android:theme instead." }
                }
            }
            a.recycle()
            if ((themeId != 0 && ((thisCtx !is ContextThemeWrapper || thisCtx.themeResId != themeId)))) {
                thisCtx = ContextThemeWrapper(thisCtx, themeId)
            }
            return thisCtx
        }
    }
}
