package io.nichijou.oops.pref

import io.nichijou.oops.Oops
import kotlin.reflect.KProperty


class FloatPref(val value: Float, override val key: String) : DelegatePref<Float>() {

    override fun get(property: KProperty<*>, thisRef: Oops): Float {
        return thisRef.prefs.getFloat(key, value)
    }

    override fun apply(property: KProperty<*>, value: Float, thisRef: Oops) {
        thisRef.prefs.edit().putFloat(key, value).apply()
    }

    override fun applyAll(property: KProperty<*>, value: Float, thisRef: Oops) {
        thisRef.prefsEditor.putFloat(key, value)
    }
}
