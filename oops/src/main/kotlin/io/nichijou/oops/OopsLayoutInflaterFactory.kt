package io.nichijou.oops

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IdRes

interface OopsLayoutInflaterFactory {
    fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet?, @IdRes viewId: Int): View?
}