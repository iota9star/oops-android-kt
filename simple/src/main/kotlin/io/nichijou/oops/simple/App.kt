package io.nichijou.oops.simple

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        LeakCanary.install(this)
    }
}