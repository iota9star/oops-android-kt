package io.nichijou.oops

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

interface OopsViewLifeAndLive : LifecycleOwner {

    fun howToLive() {}

    fun startOopsLife() {
        howToLive()
        getOopsLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    fun endOopsLife() {
        getOopsLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    fun resumeLife() {
        getOopsLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    fun pauseLife() {
        getOopsLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    fun resumeOrPauseLife(resume: Boolean) {
        if (resume) {
            resumeLife()
        } else {
            pauseLife()
        }
    }

    fun getOopsLifecycleRegistry() = lifecycle as LifecycleRegistry

    fun getOopsViewModel(): OopsViewModel
}