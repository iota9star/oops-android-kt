package io.nichijou.oops

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IdRes

interface ViewInflaterFactory {
  fun createView(parent: View?, name: String, context: Context, tag: String, tagValue: String, attrs: AttributeSet, @IdRes viewId: Int): View?
}
