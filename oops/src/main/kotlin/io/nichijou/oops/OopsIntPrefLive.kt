package io.nichijou.oops

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

class OopsIntPrefLive(private val prefs: SharedPreferences, private val key: String) : LiveData<Int>(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var lastValue: Int


    init {
        lastValue = getNewValue()
        postValue(lastValue)
    }

    private fun getNewValue() = prefs.getInt(key, 0)

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
        prefs.registerOnSharedPreferenceChangeListener(this)
        setNewValue()
    }

    override fun onInactive() {
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }
}


