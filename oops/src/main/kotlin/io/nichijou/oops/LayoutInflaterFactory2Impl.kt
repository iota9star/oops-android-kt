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
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.TintContextWrapper
import androidx.appcompat.widget.VectorEnabledTintResources
import androidx.collection.ArrayMap
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import com.google.android.material.internal.NavigationMenuItemView
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.attrValue
import io.nichijou.oops.ext.loge
import io.nichijou.oops.ext.logi
import io.nichijou.oops.ext.resId
import io.nichijou.oops.widget.ActionMenuItemView
import io.nichijou.oops.widget.AutoCompleteTextView
import io.nichijou.oops.widget.BorderlessButton
import io.nichijou.oops.widget.BottomAppBar
import io.nichijou.oops.widget.BottomNavigationView
import io.nichijou.oops.widget.Button
import io.nichijou.oops.widget.CardView
import io.nichijou.oops.widget.CheckBox
import io.nichijou.oops.widget.CheckedTextView
import io.nichijou.oops.widget.CollapsingToolbarLayout
import io.nichijou.oops.widget.ConstraintLayout
import io.nichijou.oops.widget.DialogButton
import io.nichijou.oops.widget.DialogTitle
import io.nichijou.oops.widget.DrawerLayout
import io.nichijou.oops.widget.EditText
import io.nichijou.oops.widget.FloatingActionButton
import io.nichijou.oops.widget.FrameLayout
import io.nichijou.oops.widget.ImageButton
import io.nichijou.oops.widget.ImageView
import io.nichijou.oops.widget.LinearLayout
import io.nichijou.oops.widget.LinearLayoutCompat
import io.nichijou.oops.widget.ListView
import io.nichijou.oops.widget.MaterialBorderlessButton
import io.nichijou.oops.widget.MaterialButton
import io.nichijou.oops.widget.MaterialCardView
import io.nichijou.oops.widget.MultiAutoCompleteTextView
import io.nichijou.oops.widget.NavigationView
import io.nichijou.oops.widget.NestedScrollView
import io.nichijou.oops.widget.ProgressBar
import io.nichijou.oops.widget.RadioButton
import io.nichijou.oops.widget.RatingBar
import io.nichijou.oops.widget.RecyclerView
import io.nichijou.oops.widget.RelativeLayout
import io.nichijou.oops.widget.ScrollView
import io.nichijou.oops.widget.SearchView
import io.nichijou.oops.widget.SeekBar
import io.nichijou.oops.widget.SnackbarContentLayout
import io.nichijou.oops.widget.Spinner
import io.nichijou.oops.widget.SwipeRefreshLayout
import io.nichijou.oops.widget.Switch
import io.nichijou.oops.widget.TabImageView
import io.nichijou.oops.widget.TabLayout
import io.nichijou.oops.widget.TextInputEditText
import io.nichijou.oops.widget.TextInputLayout
import io.nichijou.oops.widget.TextView
import io.nichijou.oops.widget.Toolbar
import io.nichijou.oops.widget.ViewPager
import org.xmlpull.v1.XmlPullParser
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

@SuppressLint("RestrictedApi")
internal class LayoutInflaterFactory2Impl(private val activity: AppCompatActivity, private val factory: LayoutInflaterFactory?, private val processor: PostProcessor?) : LayoutInflater.Factory2 {

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
        val viewId = ctx.resId(attrs, android.R.attr.id)
        val tag = ctx.attrValue(attrs, android.R.attr.tag)
        val tagId = ctx.resId(attrs, android.R.attr.tag)
        val tagValue = if (tagId != -1 && tagId != 0) {
            try {
                ctx.getString(tagId)
            } catch (e: Exception) {
                loge(e) { "resolve tagId($tagId)'s tagValue error" }
                ""
            }
        } else {
            ""
        }
        val enabledLiveNow = tag != "@string/ignore_oops_view" && tag != "ignore_oops_view" && tagValue != "ignore_oops_view"
        var view = factory?.onCreateView(parent, name, ctx, tag, tagValue, attrs, viewId, enabledLiveNow)
            ?: Oops.getDefaultLayoutInflaterFactory()?.onCreateView(parent, name, context, tag, tagValue, attrs, viewId, enabledLiveNow)
            ?: createOopsView(parent, name, ctx, tag, tagValue, attrs, viewId, enabledLiveNow)
        if (view == null) {
            try {
                view = activity.onCreateView(parent, name, ctx, attrs)
                if (view == null) {
                    view = activity.onCreateView(name, ctx, attrs)
                }
            } catch (e: Exception) {
                loge(e) { "${activity::class.java.canonicalName}: can't create $name." }
            }
        }
        if (view == null && attrs != null) {
            try {
                view = activity.delegate.createView(parent, name, context, attrs)
            } catch (e: Exception) {
                loge(e) { "${activity.delegate::class.java.canonicalName}: can't create $name." }
            }
        }
        if (view == null && originalContext !== ctx) {
            view = createViewFromTag(ctx, name, attrs)
        }
        if (view != null) {
            checkOnClickListener(view, attrs)
        }
        return view?.apply {
            processor?.onViewInflated(context, parent, name, tag, tagValue, attrs, viewId, this)
        }
    }

    private fun createOopsView(parent: View?, name: String, context: Context, tag: String, tagValue: String, attrs: AttributeSet?, @IdRes viewId: Int, enabledLiveNow: Boolean): View? {
        val view: View?
        var isEnableLive = enabledLiveNow
        when (name) {
            "androidx.appcompat.widget.DialogTitle" -> {
                view = DialogTitle(context, attrs)
                verifyNotNull(view, name)
            }
            "SearchView", "androidx.appcompat.widget.SearchView" -> {
                view = SearchView(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "com.google.android.material.snackbar.SnackbarContentLayout" -> {
                view = SnackbarContentLayout(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "TextView", "androidx.appcompat.widget.AppCompatTextView" -> {
                val parentName = if (parent == null) "" else parent::class.java.toString()
                view = when {
                    parentName == "com.google.android.material.tabs.TabLayout.TabView"
                        || parentName == "com.google.android.material.internal.NavigationMenuView"
                        || (parentName.endsWith("SnackBarContentLayout") && context.attrValue(attrs, android.R.attr.id) == "@id/snackbar_text")
                    -> {
                        logi { "this TextView is the child of $parentName, disable live." }
                        isEnableLive = false
                        TextView(context, attrs, false)
                    }
                    context.attrValue(attrs, android.R.attr.textAppearance) == "@android:style/TextAppearance.Toast" && context.attrValue(attrs, android.R.attr.id) == "@android:id/message" -> {
                        logi { "this TextView is the child of Toast, disable live." }
                        isEnableLive = false
                        TextView(context, attrs, false)
                    }
                    parent is android.widget.LinearLayout && context.attrValue(attrs, android.R.attr.id) == "@android:id/message" -> {
                        logi { "this is a dialog message TextView, disable live." }
                        isEnableLive = false
                        TextView(context, attrs, false)
                    }
                    else -> TextView(context, attrs, enabledLiveNow)
                }
                verifyNotNull(view, name)
            }
            "CheckBox", "androidx.appcompat.widget.AppCompatCheckBox" -> {
                view = CheckBox(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "RadioButton", "androidx.appcompat.widget.AppCompatRadioButton" -> {
                view = RadioButton(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "CheckedTextView", "androidx.appcompat.widget.AppCompatCheckedTextView" -> {
                view = if (parent is NavigationMenuItemView) {
                    logi { "this CheckedTextView is the child of com.google.android.material.internal.NavigationMenuItemView, disable live." }
                    isEnableLive = false
                    CheckedTextView(context, attrs, false)
                } else {
                    CheckedTextView(context, attrs, enabledLiveNow)
                }
                verifyNotNull(view, name)
            }
            "ImageView", "androidx.appcompat.widget.AppCompatImageView" -> {
                view = when {
                    isSearchIcon(viewId) -> {
                        logi { "this ImageView is the child of SearchView, disable live." }
                        isEnableLive = false
                        ImageView(context, attrs, false)
                    }
                    parent != null && parent::class.java.canonicalName == "com.google.android.material.tabs.TabLayout.TabView" -> TabImageView(context, attrs)
                    else -> ImageView(context, attrs, enabledLiveNow)
                }
                verifyNotNull(view, name)
            }
            "ImageButton", "androidx.appcompat.widget.AppCompatImageButton" -> {
                view = ImageButton(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "Button", "androidx.appcompat.widget.AppCompatButton" -> {
                view = when {
                    viewId == android.R.id.button1
                        || viewId == android.R.id.button2
                        || viewId == android.R.id.button3
                    -> DialogButton(context, attrs, enabledLiveNow)
                    viewId == com.google.android.material.R.id.snackbar_action -> {
                        logi { "this Button is the child of SnackBarContentLayout, disable live." }
                        isEnableLive = false
                        BorderlessButton(context, attrs, false)
                    }
                    isBorderlessButton(context, attrs) -> BorderlessButton(context, attrs, enabledLiveNow)
                    else -> Button(context, attrs, enabledLiveNow)
                }
                verifyNotNull(view, name)
            }
            "com.google.android.material.button.MaterialButton" -> {
                view = if (isBorderlessButton(context, attrs)) MaterialBorderlessButton(context, attrs, enabledLiveNow)
                else MaterialButton(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "Toolbar", "androidx.appcompat.widget.Toolbar" -> {
                view = Toolbar(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "com.google.android.material.floatingactionbutton.FloatingActionButton" -> {
                view = FloatingActionButton(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "com.google.android.material.bottomappbar.BottomAppBar" -> {
                view = BottomAppBar(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "androidx.drawerlayout.widget.DrawerLayout" -> {
                view = DrawerLayout(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "Switch", "androidx.appcompat.widget.SwitchCompat" -> {
                view = Switch(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "androidx.appcompat.view.menu.ActionMenuItemView" -> {
                view = ActionMenuItemView(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "com.google.android.material.tabs.TabLayout" -> {
                view = TabLayout(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "ProgressBar" -> {
                view = ProgressBar(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "EditText", "androidx.appcompat.widget.AppCompatEditText" -> {
                view = EditText(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "com.google.android.material.navigation.NavigationView" -> {
                view = NavigationView(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "androidx.cardview.widget.CardView" -> {
                view = CardView(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "com.google.android.material.card.MaterialCardView" -> {
                view = MaterialCardView(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "com.google.android.material.bottomnavigation.BottomNavigationView" -> {
                view = BottomNavigationView(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "com.google.android.material.appbar.CollapsingToolbarLayout" -> {
                view = CollapsingToolbarLayout(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "com.google.android.material.textfield.TextInputEditText" -> {
                view = TextInputEditText(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "com.google.android.material.textfield.TextInputLayout" -> {
                view = TextInputLayout(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "SeekBar", "androidx.appcompat.widget.AppCompatSeekBar" -> {
                view = SeekBar(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "RatingBar", "androidx.appcompat.widget.AppCompatRatingBar" -> {
                view = RatingBar(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "Spinner", "androidx.appcompat.widget.AppCompatSpinner" -> {
                view = Spinner(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "androidx.viewpager.widget.ViewPager" -> {
                view = ViewPager(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "androidx.core.widget.NestedScrollView" -> {
                view = NestedScrollView(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "androidx.recyclerview.widget.RecyclerView" -> {
                view = RecyclerView(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "ListView" -> {
                view = ListView(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "ScrollView" -> {
                view = ScrollView(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "androidx.swiperefreshlayout.widget.SwipeRefreshLayout" -> {
                view = SwipeRefreshLayout(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "LinearLayout" -> {
                view = LinearLayout(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "RelativeLayout" -> {
                view = RelativeLayout(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "FrameLayout" -> {
                view = FrameLayout(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "androidx.constraintlayout.widget.ConstraintLayout" -> {
                view = ConstraintLayout(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "androidx.appcompat.widget.LinearLayoutCompat" -> {
                view = LinearLayoutCompat(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "AutoCompleteTextView", "androidx.appcompat.widget.AppCompatAutoCompleteTextView" -> {
                view = AutoCompleteTextView(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            "MultiAutoCompleteTextView", "androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView" -> {
                view = MultiAutoCompleteTextView(context, attrs, enabledLiveNow)
                verifyNotNull(view, name)
            }
            else -> view = null
        }
        return view?.apply {
            processor?.onOopsViewInflated(context, parent, name, tag, tagValue, attrs, viewId, view)
            if (!isEnableLive) return@apply
            val backgroundAttrValue = this.context.attrValue(attrs, android.R.attr.background)
            Oops.living(this.activity()).live(backgroundAttrValue)?.observe(this as OopsLifecycleOwner, Observer {
                if (!needlessBackgroundColor(this)) {
                    this.setBackgroundColor(it)
                }
            })
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

    private fun verifyNotNull(view: View?, name: String) {
        if (view == null) {
            throw IllegalStateException("${this.javaClass.name} asked to inflate view for <$name>, but returned null")
        }
        logi { "$name => ${view::class.java.canonicalName}" }
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
