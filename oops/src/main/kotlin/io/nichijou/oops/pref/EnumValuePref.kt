package io.nichijou.oops.pref

import android.content.SharedPreferences
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


class EnumValuePref<T : Enum<*>>(enumClass: KClass<T>, private val value: T, override val key: String) : DelegatePref<T>() {

    private val enumConstants = enumClass.java.enumConstants

    override fun get(property: KProperty<*>, prefs: SharedPreferences): T {
        return enumConstants.first { it.name == prefs.getString(key, value.name) }
    }

    override fun apply(property: KProperty<*>, value: T, prefs: SharedPreferences) {
        prefs.edit().putString(key, value.name).apply()
    }

    override fun applyAll(property: KProperty<*>, value: T, editor: SharedPreferences.Editor) {
        editor.putString(key, value.name)
    }
}
