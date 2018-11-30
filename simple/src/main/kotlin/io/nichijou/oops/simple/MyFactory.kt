package io.nichijou.oops.simple

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.nichijou.oops.LayoutInflaterFactory

class MyFactory : LayoutInflaterFactory {
    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet?, viewId: Int, enableNow: Boolean): View? {
        return null
    }

}