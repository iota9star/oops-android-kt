package io.nichijou.oops

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

interface OopsLifecycleOwner : LifecycleOwner {

    fun liveInOops() {}

    fun handleOopsLifeStart() {
        (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    fun handleOopsLifeStop() {
        (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    fun handleOopsLifeDestroy() {
        (lifecycle as LifecycleRegistry).handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    fun handleOopsLifeStartOrStop(onStart: Boolean) {
        if (onStart) {
            handleOopsLifeStart()
        } else {
            handleOopsLifeStop()
        }
    }

    fun isLiveDestroyed() = lifecycle.currentState == Lifecycle.State.DESTROYED
    fun isLiveStarted() = lifecycle.currentState == Lifecycle.State.STARTED
}