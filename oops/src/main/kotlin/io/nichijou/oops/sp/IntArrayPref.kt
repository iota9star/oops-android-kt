package io.nichijou.oops.sp

import android.content.SharedPreferences
import kotlin.reflect.KProperty

class IntArrayPref(val value: IntArray, override val key: String) : DelegatePref<IntArray>() {
  override fun get(property: KProperty<*>, prefs: SharedPreferences): IntArray {
    return prefs.getString(key, value.joinToString(","))!!.split(",").map { it.trim().toInt() }
      .toIntArray()
  }

  override fun apply(property: KProperty<*>, value: IntArray, prefs: SharedPreferences) {
    prefs.edit().putString(key, value.joinToString(",")).apply()
  }

  override fun applyAll(property: KProperty<*>, value: IntArray, editor: SharedPreferences.Editor) {
    editor.putString(key, value.joinToString(","))
  }
}
