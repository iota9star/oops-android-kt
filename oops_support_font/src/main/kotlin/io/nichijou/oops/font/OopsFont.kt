package io.nichijou.oops.font

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.nichijou.oops.Oops
import io.nichijou.oops.pref.Pref
import kotlin.properties.ReadWriteProperty

object OopsFont : Pref() {
    private const val KEY_FONT_FILE_PATH = "oops_signed.font_file_path"
    private const val KEY_FONT_FILE_PATH_VERSION = "oops_signed.font_file_path_v"
    private const val KEY_FONT_ASSET_PATH = "oops_signed.font_asset_path"
    private const val KEY_FONT_ASSET_PATH_VERSION = "oops_signed.font_asset_path_v"
    override val prefs: SharedPreferences = Oops.immed().prefs
    override val prefsEditor: SharedPreferences.Editor = Oops.immed().prefsEditor
    override var transaction: Boolean = false
    override var transactionTime: Long = 0

    var assetPath by fontPref("", KEY_FONT_ASSET_PATH_VERSION, KEY_FONT_ASSET_PATH)
    var filePath by fontPref("", KEY_FONT_FILE_PATH_VERSION, KEY_FONT_FILE_PATH)

    internal fun assetsPathVersion() = prefs.getLong(KEY_FONT_ASSET_PATH_VERSION, 0L)
    internal fun filePathVersion() = prefs.getLong(KEY_FONT_FILE_PATH_VERSION, -1L)

    @JvmStatic
    fun living(activity: AppCompatActivity) = ViewModelProviders.of(activity).get(OopsFontViewModel::class.java)

    @JvmStatic
    fun living(fragment: Fragment) = ViewModelProviders.of(fragment).get(OopsFontViewModel::class.java)

    @JvmStatic
    fun attach(activity: AppCompatActivity) {

    }

    private fun fontPref(value: String, versionKey: String, key: String): ReadWriteProperty<OopsFont, String> = FontPref(value, versionKey, key)
}