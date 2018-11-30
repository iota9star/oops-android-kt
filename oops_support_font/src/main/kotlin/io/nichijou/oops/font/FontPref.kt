package io.nichijou.oops.font

import android.content.SharedPreferences
import io.nichijou.oops.pref.DelegatePref
import kotlin.reflect.KProperty

internal class FontPref(private val value: String, private val versionKey: String, override val key: String) : DelegatePref<String>() {
    override fun get(property: KProperty<*>, prefs: SharedPreferences): String {
        return prefs.getString(key, value)
    }

    override fun apply(property: KProperty<*>, value: String, prefs: SharedPreferences) {
        prefs.edit()
            .putString(key, value)
            .putLong(versionKey, System.currentTimeMillis())
            .apply()
    }

    override fun applyAll(property: KProperty<*>, value: String, editor: SharedPreferences.Editor) {
        editor.putString(key, value).putLong(versionKey, System.currentTimeMillis())
    }
}
