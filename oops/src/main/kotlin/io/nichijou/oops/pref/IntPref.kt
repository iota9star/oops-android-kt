package io.nichijou.oops.pref

import io.nichijou.oops.Oops
import kotlin.reflect.KProperty

class IntPref(val value: Int, override val key: String) : DelegatePref<Int>() {
    override fun get(property: KProperty<*>, thisRef: Oops): Int {
        return thisRef.prefs.getInt(key, value)
    }

    override fun apply(property: KProperty<*>, value: Int, thisRef: Oops) {
        thisRef.prefs.edit().putInt(key, value).apply()
    }

    override fun applyAll(property: KProperty<*>, value: Int, thisRef: Oops) {
        thisRef.prefsEditor.putInt(key, value)
    }
}
