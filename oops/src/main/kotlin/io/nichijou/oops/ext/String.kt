package io.nichijou.oops.ext

import io.nichijou.oops.KEY_COLLAPSING_TOOLBAR_DOMINANT_COLOR
import io.nichijou.oops.KEY_DISABLE_AUTO_NAV_BAR_COLOR
import io.nichijou.oops.KEY_DISABLE_AUTO_STATUS_BAR_COLOR

internal fun String.attrValueKey(): String = "oops_signed." + this.replace("?", "_q_").replace("/", "_s_")

internal fun String.disableStatusBarKey(): String = "$KEY_DISABLE_AUTO_STATUS_BAR_COLOR.$this"

internal fun String.disableNavBarKey(): String = "$KEY_DISABLE_AUTO_NAV_BAR_COLOR.$this"

internal fun String.collapsingToolbarDominantColorKey(): String = "$KEY_COLLAPSING_TOOLBAR_DOMINANT_COLOR.$this"
