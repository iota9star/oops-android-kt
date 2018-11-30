package io.nichijou.oops.font

import android.content.Context
import android.graphics.Typeface


internal object TypefaceCache {

    private val caches = hashMapOf<String, Typeface>()

    fun fromAsset(context: Context, path: String): Typeface? {
        val signedKey = "#signed_@asset_$path"
        var typeface = caches[signedKey]
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.assets, path)
            } catch (e: Exception) {
                return null
            }
            caches[signedKey] = typeface
        }
        return typeface
    }

    fun fromFile(path: String): Typeface? {
        val signedKey = "#signed_@file_$path"
        var typeface = caches[signedKey]
        if (typeface == null) {
            try {
                typeface = Typeface.createFromFile(path)
            } catch (e: Exception) {
                return null
            }
            caches[signedKey] = typeface
        }
        return typeface
    }
}