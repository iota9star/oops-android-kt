package io.nichijou.oops

import android.content.ContentProvider
import android.content.ContentValues
import android.net.Uri


internal class OopsInitProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        context?.apply { Oops.init(this.applicationContext) }
        return true
    }

    override fun insert(p0: Uri?, p1: ContentValues?) = null

    override fun query(p0: Uri?, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?) = null

    override fun update(p0: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?) = 0

    override fun delete(p0: Uri?, p1: String?, p2: Array<out String>?) = 0

    override fun getType(p0: Uri?): String = ""
}