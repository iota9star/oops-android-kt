package io.nichijou.oops.pref

import io.nichijou.oops.Oops
import kotlin.reflect.KProperty

class LongPref(val value: Long, override val key: String) : DelegatePref<Long>() {

    override fun get(property: KProperty<*>, thisRef: Oops): Long {
        return thisRef.prefs.getLong(key, value)
    }

    override fun apply(property: KProperty<*>, value: Long, thisRef: Oops) {
        thisRef.prefs.edit().putLong(key, value).apply()
    }

    override fun applyAll(property: KProperty<*>, value: Long, thisRef: Oops) {
        thisRef.prefsEditor.putLong(key, value)
    }
}
