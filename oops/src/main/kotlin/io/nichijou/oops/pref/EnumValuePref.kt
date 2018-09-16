package io.nichijou.oops.pref

import io.nichijou.oops.Oops
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


class EnumValuePref<T : Enum<*>>(enumClass: KClass<T>, private val value: T, override val key: String) : DelegatePref<T>() {

    private val enumConstants = enumClass.java.enumConstants

    override fun get(property: KProperty<*>, thisRef: Oops): T {
        return enumConstants.first { it.name == thisRef.prefs.getString(key, value.name) }
    }

    override fun apply(property: KProperty<*>, value: T, thisRef: Oops) {
        thisRef.prefs.edit().putString(key, value.name).apply()
    }

    override fun applyAll(property: KProperty<*>, value: T, thisRef: Oops) {
        thisRef.prefsEditor.putString(key, value.name)
    }
}
