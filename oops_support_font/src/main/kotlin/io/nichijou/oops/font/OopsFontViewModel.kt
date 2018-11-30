package io.nichijou.oops.font

import androidx.lifecycle.ViewModel
import io.nichijou.oops.DelegatePrefLive

class OopsFontViewModel : ViewModel() {
    val filePath by lazy {
        DelegatePrefLive(OopsFont.prefs, OopsFont::filePath)
    }
    val assetPath by lazy {
        DelegatePrefLive(OopsFont.prefs, OopsFont::assetPath)
    }
}