package io.nichijou.oops.pref

import android.os.SystemClock
import io.nichijou.oops.Oops
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


abstract class DelegatePref<T> : ReadWriteProperty<Oops, T>, PrefKey {

    abstract override val key: String
    private var lastUpdate: Long = 0
    private var value: T? = null

    override operator fun getValue(thisRef: Oops, property: KProperty<*>): T {
        if (!thisRef.transaction) {
            return get(property, thisRef)
        }
        if (lastUpdate < thisRef.transactionStartTime) {
            value = get(property, thisRef)
            lastUpdate = SystemClock.uptimeMillis()
        }
        return value ?: throw IllegalAccessException("can't get value($key)... ")
    }

    override operator fun setValue(thisRef: Oops, property: KProperty<*>, value: T) {
        if (thisRef.transaction) {
            this.value = value
            lastUpdate = SystemClock.uptimeMillis()
            applyAll(property, value, thisRef)
        } else {
            apply(property, value, thisRef)
        }
    }

    abstract fun get(property: KProperty<*>, thisRef: Oops): T
    abstract fun apply(property: KProperty<*>, value: T, thisRef: Oops)
    abstract fun applyAll(property: KProperty<*>, value: T, thisRef: Oops)
}

