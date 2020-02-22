package io.nichijou.oops.sp

import android.content.SharedPreferences
import kotlin.reflect.KProperty

class IntPref(val value: Int, override val key: String) : DelegatePref<Int>() {
  override fun get(property: KProperty<*>, prefs: SharedPreferences): Int {
    return prefs.getInt(key, value)
  }

  override fun apply(property: KProperty<*>, value: Int, prefs: SharedPreferences) {
    prefs.edit().putInt(key, value).apply()
  }

  override fun applyAll(property: KProperty<*>, value: Int, editor: SharedPreferences.Editor) {
    editor.putInt(key, value)
  }
}
