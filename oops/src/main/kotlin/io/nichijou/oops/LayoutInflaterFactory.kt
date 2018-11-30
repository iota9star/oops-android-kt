package io.nichijou.oops

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IdRes

interface LayoutInflaterFactory {
    fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet?, @IdRes viewId: Int, enableNow: Boolean): View?
}