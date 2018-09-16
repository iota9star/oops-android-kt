package io.nichijou.oops

import android.content.SharedPreferences
import androidx.lifecycle.GenericLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import io.nichijou.oops.pref.PrefKey
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

internal class OopsLive<T>(owner: LifecycleOwner, private val prefs: SharedPreferences, private val property: KProperty0<T>) : LiveData<T>(), SharedPreferences.OnSharedPreferenceChangeListener, GenericLifecycleObserver {

    private var lastValue: T

    private val key: String

    init {
        property.isAccessible = true
        key = (property.getDelegate() as PrefKey).key
        property.isAccessible = false
        lastValue = property.get()
        this.value = lastValue
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (source.lifecycle.currentState == Lifecycle.State.RESUMED) {
            val value = property.get()
            if (lastValue != value) {
                lastValue = value
                postValue(lastValue)
            }
        }
    }

    override fun onSharedPreferenceChanged(prefs: SharedPreferences, changed: String) {
        if (changed == key) {
            lastValue = property.get()
            postValue(lastValue)
        }
    }

    override fun onActive() {
        this.prefs.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onInactive() {
        this.prefs.unregisterOnSharedPreferenceChangeListener(this)
    }
}


