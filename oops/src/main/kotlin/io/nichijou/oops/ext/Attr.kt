package io.nichijou.oops.ext

import android.content.Context
import android.util.AttributeSet

fun AttributeSet?.getStringValue(context: Context, namespace: String?, name: String): String {
  if (this == null) return ""
  val value = getAttributeValue(namespace, name) ?: ""
  return when {
    value.matches(Regex("^@\\d+$")) -> context.resources.getNonNullString(value)
    value.matches(Regex("^[?]\\d+$")) -> "?" + context.resources.getNonNullResourceName(value.substring(1).toInt())
    else -> value
  }
}

fun AttributeSet?.getStringValues(context: Context, namespace: String?, names: Array<String>): HashMap<String, String> {
  if (this == null) return HashMap(0)
  val values = HashMap<String, String>(names.size)
  var value: String
  for (name in names) {
    value = getAttributeValue(namespace, name) ?: ""
    values[name] = when {
      value.matches(Regex("^@\\d+$")) -> context.getString(value.substring(1).toInt())
      value.matches(Regex("^[?]\\d+$")) -> "?" + context.resources.getNonNullResourceName(value.substring(1).toInt())
      else -> value
    }
  }
  return values
}
