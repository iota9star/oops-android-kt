package io.nichijou.oops

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IdRes

interface PostProcessor {
    fun onFinishInflate(context: Context, parent: View?, name: String, tag: String, tagValue: String, attrs: AttributeSet?, @IdRes viewId: Int, createdView: View?)
}