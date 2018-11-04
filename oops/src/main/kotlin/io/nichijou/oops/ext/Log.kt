package io.nichijou.oops.ext

import android.util.Log
import io.nichijou.oops.BuildConfig

inline fun <reified T> T.logi(message: () -> String) {
    if (BuildConfig.DEBUG) Log.i(T::class.simpleName, message())
}

inline fun <reified T> T.loge(error: Throwable, message: () -> String) = Log.e(T::class.simpleName, message(), error)