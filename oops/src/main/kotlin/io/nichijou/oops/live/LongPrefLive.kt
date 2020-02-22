package io.nichijou.oops.live

import android.content.SharedPreferences
import kotlinx.coroutines.launch

class LongPrefLive(private val prefs: SharedPreferences, private val key: String) : CoroutinesIOLiveData<Long>(), SharedPreferences.OnSharedPreferenceChangeListener {

  init {
    launch {
      postValue(getPrefValue())
    }
  }

  private fun getPrefValue() = prefs.getLong(key, 0)

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


