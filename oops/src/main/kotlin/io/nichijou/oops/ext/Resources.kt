package io.nichijou.oops.ext

import android.content.res.Resources

fun Resources.getNonNullResourceName(resId: Int): String {
  if (resId == 0) {
    return ""
  }
  return try {
    getResourceName(resId)
  } catch (e: Exception) {
    loge("Resources.getResourceName is error, return empty String...", e)
    ""
  }
}

internal fun Resources.getNonNullString(mayStrId: String): String {
  return try {
    getString(mayStrId.substring(1).toInt())
  } catch (e: Exception) {
    loge("Resources.getString is error, return original String: $mayStrId", e)
    mayStrId
  }
}
