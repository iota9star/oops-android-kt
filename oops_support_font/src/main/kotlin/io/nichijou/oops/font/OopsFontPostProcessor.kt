package io.nichijou.oops.font

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.PostProcessor
import io.nichijou.oops.ext.activity

class OopsFontPostProcessor : PostProcessor {
    override fun onOopsViewInflated(context: Context, parent: View?, name: String, tag: String, tagValue: String, attrs: AttributeSet?, viewId: Int, createdView: View) {
        if (tag != "@string/ignore_oops_font"
            && tag != "ignore_oops_font"
            && tagValue != "ignore_oops_font"
            && createdView is OopsLifecycleOwner) {
            when (createdView) {
                is TextView -> createdView.bindTypeface()
            }
        }
    }

    override fun onViewInflated(context: Context, parent: View?, name: String, tag: String, tagValue: String, attrs: AttributeSet?, viewId: Int, createdView: View) {
    }
}

internal fun TextView.bindTypeface() {
    if (this !is OopsLifecycleOwner) return
    val activity = this.activity()
    val owner = this as OopsLifecycleOwner
    val style = this.typeface?.style ?: Typeface.NORMAL
    OopsFont.living(activity).assetPath.observe(owner, Observer {
        if (OopsFont.filePathVersion() < OopsFont.assetsPathVersion()) {
            val typeface = if (it.isNotBlank())
                TypefaceCache.fromAsset(this.context, it)
            else null
            setTypeface(typeface, style)
            requestLayout()
        }
    })
    OopsFont.living(activity).filePath.observe(owner, Observer {
        if (OopsFont.filePathVersion() > OopsFont.assetsPathVersion()) {
            val typeface = if (it.isNotBlank())
                TypefaceCache.fromFile(it)
            else null
            setTypeface(typeface, style)
            requestLayout()
        }
    })
}