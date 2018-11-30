package io.nichijou.oops.ext

import io.nichijou.oops.OopsPrefsKey

internal fun String.attrValueKey(): String = "oops_signed." + this.replace("?", "..q..").replace("/", "..s..")


internal fun String.statusBarColorKey(): String = OopsPrefsKey.KEY_STATUS_BAR_COLOR + "." + this


internal fun String.navBarColorKey(): String = OopsPrefsKey.KEY_NAV_BAR_COLOR + "." + this


internal fun String.collapsingToolbarDominantColorKey(): String = OopsPrefsKey.KEY_COLLAPSING_TOOLBAR_DOMINANT_COLOR + "." + this
