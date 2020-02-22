package io.nichijou.oops.sp

import android.content.SharedPreferences
import android.os.SystemClock
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class DelegatePref<T> : ReadWriteProperty<Pref, T>, PrefKey {
  abstract override val key: String
  private var lastUpdate: Long = 0
  private var value: T? = null

  override operator fun getValue(thisRef: Pref, property: KProperty<*>): T {
    if (!thisRef.transaction) {
      return get(property, thisRef.prefs)
    }
    if (lastUpdate < thisRef.transactionTime) {
      value = get(property, thisRef.prefs)
      lastUpdate = SystemClock.uptimeMillis()
    }
    return value ?: throw IllegalAccessException("can't get value($key)... ")
  }

  override operator fun setValue(thisRef: Pref, property: KProperty<*>, value: T) {
    if (thisRef.transaction) {
      this.value = value
      lastUpdate = SystemClock.uptimeMillis()
      applyAll(property, value, thisRef.prefsEditor)
    } else {
      apply(property, value, thisRef.prefs)
    }
  }

  abstract fun get(property: KProperty<*>, prefs: SharedPreferences): T
  abstract fun apply(property: KProperty<*>, value: T, prefs: SharedPreferences)
  abstract fun applyAll(property: KProperty<*>, value: T, editor: SharedPreferences.Editor)
}

