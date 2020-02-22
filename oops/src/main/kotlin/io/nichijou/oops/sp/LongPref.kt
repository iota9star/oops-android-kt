package io.nichijou.oops.sp

import android.content.SharedPreferences
import kotlin.reflect.KProperty

class LongPref(val value: Long, override val key: String) : DelegatePref<Long>() {
  override fun get(property: KProperty<*>, prefs: SharedPreferences): Long {
    return prefs.getLong(key, value)
  }

  override fun apply(property: KProperty<*>, value: Long, prefs: SharedPreferences) {
    prefs.edit().putLong(key, value).apply()
  }

  override fun applyAll(property: KProperty<*>, value: Long, editor: SharedPreferences.Editor) {
    editor.putLong(key, value)
  }
}
