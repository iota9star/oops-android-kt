package io.nichijou.oops.simple

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.nichijou.oops.OopsLayoutInflaterFactory

class MyFactory : OopsLayoutInflaterFactory {
    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet?, viewId: Int): View? {
        return when (name) {
            "TextView" -> CustomTextView(context, attrs)
            else -> null
        }
    }
}