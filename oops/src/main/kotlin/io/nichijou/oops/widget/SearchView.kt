package io.nichijou.oops.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import io.nichijou.oops.OopsLifecycleOwner
import io.nichijou.oops.ext.pauseOopsLife
import io.nichijou.oops.ext.resumeOopsLife

@SuppressLint("ViewConstructor")
open class SearchView @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null) : SearchView(context, attrs), OopsLifecycleOwner {
  private val lifecycleRegistry = LifecycleRegistry(this)
  override fun liveInOops() {
    //    Oops.living(this.activity()).toolbarIconColor.observe(this, Observer(this::tint))
  }

  override fun getLifecycle(): Lifecycle = lifecycleRegistry
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    attachOopsLife()
  }

  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    if (visibility == View.VISIBLE) {
      super.onVisibilityChanged(changedView, visibility)
      changedView.resumeOopsLife()
    } else {
      changedView.pauseOopsLife()
      super.onVisibilityChanged(changedView, visibility)
    }
  }

  override fun onWindowVisibilityChanged(visibility: Int) {
    if (visibility == View.VISIBLE) {
      super.onWindowVisibilityChanged(visibility)
      resumeOopsLife()
    } else {
      pauseOopsLife()
      super.onWindowVisibilityChanged(visibility)
    }
  }

  override fun onDetachedFromWindow() {
    detachOopsLife()
    super.onDetachedFromWindow()
  }
}
