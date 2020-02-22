package io.nichijou.oops.live

import android.content.SharedPreferences
import kotlinx.coroutines.launch

class IntPrefLive(private val prefs: SharedPreferences, private val key: String) : CoroutinesIOLiveData<Int>(), SharedPreferences.OnSharedPreferenceChangeListener {

  init {
    launch {
      postValue(getPrefValue())
    }
  }

  private fun getPrefValue() = prefs.getInt(key, 0)

  override fun onSharedPreferenceChanged(prefs: SharedPreferences, changed: String) {
    if (changed == key) {
      ifChangeUpdate()
    }
  }

  private fun ifChangeUpdate() {
    launch {
      val newValue = getPrefValue()
      if (value != newValue) postValue(newValue)
    }
  }

  override fun onActive() {
    prefs.registerOnSharedPreferenceChangeListener(this)
    ifChangeUpdate()
  }

  override fun onInactive() {
    prefs.unregisterOnSharedPreferenceChangeListener(this)
    super.onInactive()
  }
}


