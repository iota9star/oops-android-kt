package io.nichijou.oops

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.TintContextWrapper
import androidx.appcompat.widget.VectorEnabledTintResources
import androidx.collection.ArrayMap
import androidx.core.view.ViewCompat
import io.nichijou.oops.ext.attrValue
import io.nichijou.oops.ext.loge
import io.nichijou.oops.ext.resId
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import kotlin.collections.set

@SuppressLint("RestrictedApi")
internal class OopsInflaterFactory2(private val factory: ViewInflaterFactory?, private val processor: ViewInflatedProcessor?) : LayoutInflater.Factory2 {

  override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
    return createView(parent, name, context, attrs, VectorEnabledTintResources.shouldBeUsed())
  }

  override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
    return onCreateView(null, name, context, attrs)
  }

  private val oopsViewInflater by lazy(LazyThreadSafetyMode.NONE) {
    OopsViewInflater()
  }
  private val defaultViewInflater by lazy(LazyThreadSafetyMode.NONE) {
    DefaultViewInflater()
  }

  private fun createView(parent: View?, name: String, context: Context, attrs: AttributeSet, wrapContext: Boolean): View? {
    var ctx = context
    val originalContext = ctx
    if (wrapContext) {
      ctx = TintContextWrapper.wrap(ctx)
    }
    val viewId = context.resId(attrs, android.R.attr.id)
    val tag = context.attrValue(attrs, android.R.attr.tag)
    val tagId = context.resId(attrs, android.R.attr.tag)
    val tagValue = if (tagId != -1 && tagId != 0) {
      try {
        context.getString(tagId)
      } catch (e: Exception) {
        loge("resolve tagId($tagId)'s tagValue error", e)
        ""
      }
    } else {
      ""
    }
    val ignore = tag == "ignore_oops_view" || tagValue == "ignore_oops_view"
    var view = if (ignore) {
      defaultViewInflater.createView(parent, name, context, tag, tagValue, attrs, viewId)
    } else {
      factory?.createView(parent, name, ctx, tag, tagValue, attrs, viewId)
        ?: Oops.getDefaultViewInflaterFactory()?.createView(parent, name, ctx, tag, tagValue, attrs, viewId)
        ?: oopsViewInflater.createView(parent, name, ctx, tag, tagValue, attrs, viewId)
        ?: defaultViewInflater.createView(parent, name, context, tag, tagValue, attrs, viewId)
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

  private val mConstructorArgs = arrayOfNulls<Any>(2)

  private fun createViewFromTag(context: Context, name: String, attrs: AttributeSet): View? {
    var named = name
    if (named == "view") {
      named = attrs.getAttributeValue(null, "class")
    }
    try {
      mConstructorArgs[0] = context
      mConstructorArgs[1] = attrs

      if (-1 == named.indexOf('.')) {
        for (s in sClassPrefixList) {
          val view = createViewByPrefix(context, named, s)
          if (view != null) {
            return view
          }
        }
        return null
      } else {
        return createViewByPrefix(context, named, null)
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
    if (context !is ContextWrapper || !ViewCompat.hasOnClickListeners(view)) {
      return
    }
    val a = context.obtainStyledAttributes(attrs, sOnClickAttrs)
    val handlerName = a.getString(0)
    if (handlerName != null) {
      view.setOnClickListener(DeclaredOnClickListener(view, handlerName))
    }
    a.recycle()
  }

  @Throws(InflateException::class)
  private fun createViewByPrefix(context: Context, name: String, prefix: String?): View? {
    var constructor = sConstructorMap[name]
    return try {
      if (constructor == null) {
        val clazz = Class.forName(
          if (prefix != null) prefix + name else name,
          false,
          context.classLoader).asSubclass(View::class.java)

        constructor = clazz.getConstructor(*sConstructorSignature)
        sConstructorMap[name] = constructor
      }
      constructor.isAccessible = true
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
        resolveMethod(mHostView.context)
      }
      try {
        mResolvedMethod?.invoke(mResolvedContext, v)
      } catch (e: IllegalAccessException) {
        throw IllegalStateException(
          "Could not execute non-public method for android:onClick", e)
      } catch (e: InvocationTargetException) {
        throw IllegalStateException(
          "Could not execute method for android:onClick", e)
      }

    }

    private fun resolveMethod(context: Context?) {
      var ctx = context
      while (ctx != null) {
        try {
          if (!ctx.isRestricted) {
            val method = ctx::class.java.getMethod(mMethodName, View::class.java)
            mResolvedMethod = method
            mResolvedContext = ctx
            return
          }
        } catch (e: NoSuchMethodException) {
        }
        ctx = if (ctx is ContextWrapper) {
          ctx.baseContext
        } else {
          null
        }
      }

      val id = mHostView.id
      val idText = if (id == View.NO_ID)
        ""
      else
        " with id '" + mHostView.context.resources.getResourceEntryName(id) + "'"
      throw IllegalStateException("Could not find method " + mMethodName
        + "(View) in a parent or ancestor Context for android:onClick "
        + "attribute defined on view " + mHostView.javaClass + idText)
    }
  }

  companion object {
    private val sConstructorSignature = arrayOf(Context::class.java, AttributeSet::class.java)
    private val sOnClickAttrs = intArrayOf(android.R.attr.onClick)
    private val sClassPrefixList = arrayOf("android.widget.", "android.view.", "android.webkit.")
    private val sConstructorMap = ArrayMap<String, Constructor<out View>>()
  }
}
