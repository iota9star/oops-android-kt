package io.nichijou.oops.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import io.nichijou.oops.ext.tint

internal class TabImageView(context: Context, @Nullable attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    private var tabTextColor: ColorStateList? = null

    fun setTabTextColor(tabTextColor: ColorStateList?) {
        this.tabTextColor = tabTextColor
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable?.tint(tabTextColor))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        tabTextColor = (this.parent?.parent?.parent as? TabLayout?)?.getIconColor()
        setImageDrawable(drawable?.tint(tabTextColor))
    }
}
