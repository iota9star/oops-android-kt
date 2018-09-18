package io.nichijou.oops

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import io.nichijou.oops.ext.logi
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
            //            val value = property.get()
            //            if (lastValue != value) {
            //                lastValue = value
            //                postValue(lastValue)
            //            }
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
        logi { "key: $key   -   hashCode: ${this.hashCode()}  - onActive" }
        this.prefs.registerOnSharedPreferenceChangeListener(this)
        setNewValue()
    }

    override fun onInactive() {
        //        if (OopsPrefsKey.KEY_WINDOW_BACKGROUND_COLOR == key) {
        //            logi { "hashCode: ${this.hashCode()}  - onInactive" }
        //        }
        logi { "key: $key   -   hashCode: ${this.hashCode()}  - onInactive" }
        this.prefs.unregisterOnSharedPreferenceChangeListener(this)
    }
}


