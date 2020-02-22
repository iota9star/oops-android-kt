package io.nichijou.oops.live

import android.content.SharedPreferences
import io.nichijou.oops.sp.PrefKey
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

class DelegatePrefLive<T>(private val prefs: SharedPreferences, private val property: KProperty0<T>) : CoroutinesIOLiveData<T>(), SharedPreferences.OnSharedPreferenceChangeListener {
  private val key: String
    get() {
      property.isAccessible = true
      return (property.getDelegate() as PrefKey).key
    }

  init {
    launch {
      postValue(property.get())
    }
  }

  override fun onSharedPreferenceChanged(prefs: SharedPreferences, changed: String) {
    if (changed == key) {
      ifChangeUpdate()
    }
  }

  private fun ifChangeUpdate() {
    launch {
      val newValue = property.get()
      if (value != newValue) postValue(newValue)
    }
  }

  override fun onActive() {
    this.prefs.registerOnSharedPreferenceChangeListener(this)
    ifChangeUpdate()
  }

  override fun onInactive() {
    this.prefs.unregisterOnSharedPreferenceChangeListener(this)
    super.onInactive()
  }
}


