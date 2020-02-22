package io.nichijou.oops.simple

import android.util.Log

internal inline fun <reified T> T.logd(info: String) {
  Log.d(T::class.java.name + " => DEBUG: ", info)
}

internal inline fun <reified T> T.loge(info: String) {
  Log.e(T::class.java.name + " => ERROR: ", info)
}

internal inline fun <reified T> T.loge(info: String, throwable: Throwable?) {
  Log.e(T::class.java.name + " => ERROR: ", info, throwable)
}
