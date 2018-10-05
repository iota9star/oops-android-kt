package io.nichijou.oops

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData

class OopsSharedPreferencesLive<T>(private val prefs: SharedPreferences, private val key: String, private val getNewValue: () -> T) : MutableLiveData<T>(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var lastValue: T


    init {
        lastValue = getNewValue()
        postValue(lastValue)
    }

    override fun onSharedPreferenceChanged(prefs: SharedPreferences, changed: String) {
        if (changed == key) {
            setNewValue()
        }
    }

    private fun setNewValue() {
        val value = getNewValue()
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


