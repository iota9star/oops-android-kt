package io.nichijou.oops.ext

import android.content.res.Resources

fun Resources.getNonNullResourceName(resId: Int): String {
    if (resId == 0) {
        return ""
    }
    return try {
        getResourceName(resId)
    } catch (e: Exception) {
        loge(e) { "Resources.getResourceName is error, return empty String..." }
        ""
    }
}
