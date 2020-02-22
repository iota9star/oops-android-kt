package io.nichijou.oops.initializer

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import io.nichijou.oops.Oops

internal class OopsInitializer : ContentProvider() {
  override fun onCreate(): Boolean {
    context?.let {
      Oops.init(it.applicationContext)
    }
    return true
  }

  override fun insert(uri: Uri, values: ContentValues?): Uri? = null
  override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null
  override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
  override fun getType(uri: Uri): String? = null
}
