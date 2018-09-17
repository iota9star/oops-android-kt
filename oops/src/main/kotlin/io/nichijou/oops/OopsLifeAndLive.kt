package io.nichijou.oops

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

interface OopsLifeAndLive : LifecycleOwner {
    fun bindingLive()
    fun unbindingLive() {
        (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}