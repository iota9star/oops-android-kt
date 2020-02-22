package io.nichijou.oops.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import io.nichijou.oops.OopsThemeStore

inline fun Fragment.applyOopsThemeStore(crossinline block: OopsThemeStore.() -> Unit) {
  this.lifecycleScope.launchWhenCreated {
    ViewModelProvider(this@applyOopsThemeStore).get(OopsThemeStore::class.java).apply {
      block()
    }
  }
}
