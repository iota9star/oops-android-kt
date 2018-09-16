package io.nichijou.oops.pref

import io.nichijou.oops.Oops
import kotlin.reflect.KProperty


class BooleanPref(val value: Boolean, override val key: String) : DelegatePref<Boolean>() {

    override fun get(property: KProperty<*>, thisRef: Oops): Boolean {
        return thisRef.prefs.getBoolean(key, value)
    }

    override fun apply(property: KProperty<*>, value: Boolean, thisRef: Oops) {
        thisRef.prefs.edit().putBoolean(key, value).apply()
    }

    override fun applyAll(property: KProperty<*>, value: Boolean, thisRef: Oops) {
        thisRef.prefsEditor.putBoolean(key, value)
    }
}
