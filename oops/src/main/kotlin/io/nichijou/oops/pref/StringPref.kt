package io.nichijou.oops.pref

import io.nichijou.oops.Oops
import kotlin.reflect.KProperty

class StringPref(val value: String, override val key: String) : DelegatePref<String>() {

    override fun get(property: KProperty<*>, thisRef: Oops): String {
        return thisRef.prefs.getString(key, value)
    }

    override fun apply(property: KProperty<*>, value: String, thisRef: Oops) {
        thisRef.prefs.edit().putString(key, value).apply()
    }

    override fun applyAll(property: KProperty<*>, value: String, thisRef: Oops) {
        thisRef.prefsEditor.putString(key, value)
    }
}
