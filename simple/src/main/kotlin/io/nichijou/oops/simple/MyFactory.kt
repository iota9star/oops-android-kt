package io.nichijou.oops.simple

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.nichijou.oops.ViewInflaterFactory

class MyFactory : ViewInflaterFactory {
  override fun createView(parent: View?, name: String, context: Context, tag: String, tagValue: String, attrs: AttributeSet, viewId: Int): View? {
    return null
  }

}
