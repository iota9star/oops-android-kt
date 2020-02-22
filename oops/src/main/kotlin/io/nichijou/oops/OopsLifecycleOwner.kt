package io.nichijou.oops

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

interface OopsLifecycleOwner : LifecycleOwner {
  fun liveInOops() {}

  fun detachOopsLife() {
    (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  }

  fun attachOopsLife() {
    liveInOops()
    (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_START)
  }

  fun pauseOopsLife() {
    (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  }

  fun resumeOopsLife() {
    (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
  }

  fun viewAttachStateBindOopsLifecycle(view: View) {
    view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
      override fun onViewDetachedFromWindow(v: View) {
        detachOopsLife()
        v.removeOnAttachStateChangeListener(this)
      }

      override fun onViewAttachedToWindow(v: View) {
        attachOopsLife()
      }
    })
  }
}
