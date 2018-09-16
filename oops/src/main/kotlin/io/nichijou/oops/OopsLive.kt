package io.nichijou.oops

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import io.nichijou.oops.pref.PrefKey
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

class OopsLive<T>(private val prefs: SharedPreferences, private val property: KProperty0<T>) : MutableLiveData<T>(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var lastValue: T

    private val key: String

    init {
        property.isAccessible = true
        key = (property.getDelegate() as PrefKey).key
        property.isAccessible = false
        lastValue = property.get()
        this.value = lastValue
    }

    override fun onSharedPreferenceChanged(prefs: SharedPreferences, changed: String) {
        if (changed == key) {
            val value = property.get()
            if (lastValue != value) {
                lastValue = value
                postValue(lastValue)
            }
        }
    }

    override fun onActive() {
        this.prefs.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onInactive() {
        this.prefs.unregisterOnSharedPreferenceChangeListener(this)
    }
}


