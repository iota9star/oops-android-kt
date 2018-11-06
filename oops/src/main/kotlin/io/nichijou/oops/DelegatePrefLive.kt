package io.nichijou.oops

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import io.nichijou.oops.pref.PrefKey
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

class DelegatePrefLive<T>(private val prefs: SharedPreferences, private val property: KProperty0<T>) : LiveData<T>(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var lastValue: T

    private val key: String

    init {
        property.isAccessible = true
        key = (property.getDelegate() as PrefKey).key
        property.isAccessible = false
        lastValue = property.get()
        postValue(lastValue)
    }

    override fun onSharedPreferenceChanged(prefs: SharedPreferences, changed: String) {
        if (changed == key) {
            setNewValue()
        }
    }

    private fun setNewValue() {
        val value = property.get()
        if (lastValue != value) {
            lastValue = value
            postValue(lastValue)
        }
    }

    override fun onActive() {
        this.prefs.registerOnSharedPreferenceChangeListener(this)
        setNewValue()
    }

    override fun onInactive() {
        this.prefs.unregisterOnSharedPreferenceChangeListener(this)
    }
}


