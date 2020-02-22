package io.nichijou.oops.sp

import android.content.SharedPreferences

abstract class Pref {
  abstract val prefsEditor: SharedPreferences.Editor
  abstract val prefs: SharedPreferences
  abstract var transaction: Boolean
  abstract var transactionTime: Long
}
