package io.nichijou.oops.sp

import android.content.SharedPreferences
import kotlin.reflect.KProperty

class BooleanPref(val value: Boolean, override val key: String) : DelegatePref<Boolean>() {
  override fun get(property: KProperty<*>, prefs: SharedPreferences): Boolean {
    return prefs.getBoolean(key, value)
  }

  override fun apply(property: KProperty<*>, value: Boolean, prefs: SharedPreferences) {
    prefs.edit().putBoolean(key, value).apply()
  }

  override fun applyAll(property: KProperty<*>, value: Boolean, editor: SharedPreferences.Editor) {
    editor.putBoolean(key, value)
  }
}
