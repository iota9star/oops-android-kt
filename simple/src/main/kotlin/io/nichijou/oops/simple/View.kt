package io.nichijou.oops.simple

import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.CollapsingToolbarLayout
import io.nichijou.oops.ext.activity
import io.nichijou.oops.ext.getStatusBarHeight

fun Toolbar.topStatusBarMargin() {
    val lp = this.layoutParams as CollapsingToolbarLayout.LayoutParams
    lp.topMargin = context.activity().getStatusBarHeight()
    lp.bottomMargin
    this.layoutParams = lp
}