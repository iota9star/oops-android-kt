package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import com.google.android.material.textfield.TextInputEditText
import io.nichijou.oops.OopsLiveProvider
import io.nichijou.oops.ext.ctx


class OopsTextInputEditText : TextInputEditText, OopsLiveProvider {
    private val attrs: AttributeSet?

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        this.attrs = attrs
        registerOopsLive()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        registerOopsLive()
    }

    override fun unregisterOopsLive() {
    }

    @SuppressLint("ResourceType")
    override fun registerOopsLive() {
        if (attrs != null) {
            val ctx = this.ctx()
            val attrsArray = intArrayOf(android.R.attr.background, android.R.attr.textColor, android.R.attr.textColorHint)
            val ta = context.obtainStyledAttributes(attrs, attrsArray)
            val backgroundResId = ta.getResourceId(0, 0)
            val textColorResId = ta.getResourceId(1, 0)
            val textColorHintResId = ta.getResourceId(2, 0)
            ta.recycle()
        }
    }


}
