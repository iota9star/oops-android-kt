package io.nichijou.oops.pref

import io.nichijou.oops.Oops
import kotlin.reflect.KProperty

class IntArrayPref(val value: IntArray, override val key: String) : DelegatePref<IntArray>() {
    override fun get(property: KProperty<*>, thisRef: Oops): IntArray {
        return thisRef.prefs.getString(key, value.joinToString(",")).split(",").map { it.trim().toInt() }.toIntArray()
    }

    override fun apply(property: KProperty<*>, value: IntArray, thisRef: Oops) {
        thisRef.prefs.edit().putString(key, value.joinToString(",")).apply()
    }

    override fun applyAll(property: KProperty<*>, value: IntArray, thisRef: Oops) {
        thisRef.prefsEditor.putString(key, value.joinToString(","))
    }
}
