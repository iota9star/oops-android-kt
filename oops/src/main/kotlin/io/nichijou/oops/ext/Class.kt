package io.nichijou.oops.ext

import java.lang.reflect.Field

fun <T> Class<T>.findField(vararg nameOptions: String): Field {
  for (name in nameOptions) {
    try {
      return getDeclaredField(name)
    } catch (_: NoSuchFieldException) {
    }
  }
  throw IllegalStateException("Unable to find any of fields $nameOptions")
}
