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
import android.widget.LinearLayout
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.*
import androidx.collection.ArrayMap
import androidx.core.view.ViewCompat
import io.nichijou.oops.ext.logi
import io.nichijou.oops.ext.resId
import io.nichijou.oops.widget.*
import org.xmlpull.v1.XmlPullParser
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import io.nichijou.oops.R as MR

@SuppressLint("RestrictedApi")
open class OopsFactory2Impl : LayoutInflater.Factory2 {

    private fun shouldInheritContext(context: Context, parent: ViewParent?): Boolean {
        var thisParent: ViewParent? = parent ?: return false
        when (context) {
            is AppCompatActivity -> {
                val windowDecor = context.window.decorView
                while (true) {
                    if (thisParent == null) {
                        return true
                    } else if (thisParent === windowDecor || thisParent !is View
                            || ViewCompat.isAttachedToWindow((thisParent as View?)!!)) {
                        return false
                    }
                    thisParent = (thisParent as ViewParent).parent
                }
            }
            else -> return false
        }
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
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

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }

    private val mConstructorArgs = arrayOfNulls<Any>(2)

    private fun createView(parent: View?, name: String, context: Context, attrs: AttributeSet, inheritContext: Boolean, readAndroidTheme: Boolean, readAppTheme: Boolean, wrapContext: Boolean): View? {
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
        var view = createOopsView(name, parent, context, attrs)
        if (view != null && view.tag != null && view.tag == context.getString(MR.string.ignore_view)) {
            view = createDefaultView(name, context, attrs)
        }
        if (view == null && originalContext !== ctx) {
            view = createViewFromTag(ctx, name, attrs)
        }
        if (view != null) {
            checkOnClickListener(view, attrs)
        }
        return view
    }

    private fun createOopsView(name: String, parent: View?, context: Context, attrs: AttributeSet): View? {
        var view: View?
        val viewId = context.resId(attrs, android.R.attr.id)
        when (name) {
            "com.google.android.material.snackbar.SnackbarContentLayout" -> {
                view = OopsSnackBarContentLayout(context, attrs)
            }
            "TextView", "androidx.appcompat.widget.AppCompatTextView" -> {
                view = if (viewId == com.google.android.material.R.id.snackbar_text) {
                    OopsSnackBarTextView(context, attrs)
                } else {
                    OopsTextView(context, attrs)
                }
                verifyNotNull(view, name, false)
                if (parent is LinearLayout && view.id == android.R.id.message) {
                    view = null
                    logi { "this is a message TextView (dialog or toast) and reset to null." }
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
            "ImageView", "androidx.appcompat.widget.AppCompatImageView" -> {
                view = OopsImageView(context, attrs)
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
            "Toolbar", "androidx.appcompat.widget.Toolbar" -> {
                view = OopsToolbar(context, attrs)
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.floatingactionbutton.FloatingActionButton" -> {
                view = OopsFloatingActionButton(context, attrs)
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
            "com.google.android.material.bottomnavigation.BottomNavigationView" -> {
                view = OopsBottomNavigationView(context, attrs)
                verifyNotNull(view, name, false)
            }
            "com.google.android.material.appbar.CollapsingToolbarLayout" -> {
                view = OopsCollapsingToolbarLayout(context, attrs)
                verifyNotNull(view, name, false)
            }
//            "android.support.design.widget.TextInputEditText", "com.google.android.material.textfield.TextInputEditText" -> {
//                view = DuangTextInputEditText(activity, attrs)
//                verifyNotNull(view, name)
//            }
//            "android.support.design.widget.TextInputLayout", "com.google.android.material.textfield.TextInputLayout" -> {
//                view = DuangTextInputLayout(activity, attrs)
//                verifyNotNull(view, name)
//            }
            "SeekBar", "androidx.appcompat.widget.AppCompatSeekBar" -> {
                view = OopsSeekBar(context, attrs)
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
            else ->
                view = createView(context, name, attrs)
        }
        return view
    }

    private fun isBorderlessButton(context: Context, attrs: AttributeSet?): Boolean {
        if (attrs == null) {
            return false
        }
        val backgroundRes = context.resId(attrs, android.R.attr.background)
        if (backgroundRes == 0) {
            return false
        }
        val resName = context.resources.getResourceEntryName(backgroundRes)
        return resName.endsWith("btn_borderless_material")
    }

    private fun createDefaultView(name: String, context: Context, attrs: AttributeSet): View? {
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
                view = createView(context, name, attrs)
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

    private fun createView(context: Context, name: String, attrs: AttributeSet): View? {
        return null
    }

    private fun createViewFromTag(context: Context, name: String, attrs: AttributeSet): View? {
        var thisName = name
        if (thisName == "view") {
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

    private fun checkOnClickListener(view: View, attrs: AttributeSet) {
        val context = view.context
        if (context !is ContextWrapper || Build.VERSION.SDK_INT >= 15 && !ViewCompat.hasOnClickListeners(view)) {
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
                        if (method != null) {
                            mResolvedMethod = method
                            mResolvedContext = thisCtx
                            return
                        }
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
            val idText = if (id == View.NO_ID)
                ""
            else
                " with id '" + mHostView.context.resources.getResourceEntryName(id) + "'"
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

        private fun themifyContext(context: Context, attrs: AttributeSet, useAndroidTheme: Boolean, useAppTheme: Boolean): Context {
            var thisCtx = context
            val a = thisCtx.obtainStyledAttributes(attrs, R.styleable.View, 0, 0)
            var themeId = 0
            if (useAndroidTheme) {
                themeId = a.getResourceId(R.styleable.View_android_theme, 0)
            }
            if (useAppTheme && themeId == 0) {
                themeId = a.getResourceId(R.styleable.View_theme, 0)
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
