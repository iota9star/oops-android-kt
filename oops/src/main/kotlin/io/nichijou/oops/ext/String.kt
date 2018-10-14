package io.nichijou.oops.ext

import io.nichijou.oops.OopsPrefsKey

// eg: ?android:attr/textColorPrimary to OOPS_SIGNED__QM_android_COLON_attr_SLASH_textColorPrimary
internal fun String.oopsSignedAttrName(): String {
    return "OOPS_SIGNED_" + this.replace("?", "_QM_")
            .replace("/", "_SLASH_")
            .replace(":", "_COLON_")
}

internal fun String.oopsSignedStatusBarColorKey(): String {
    return this.replace(".", "_P_") + "_" + OopsPrefsKey.KEY_STATUS_BAR_COLOR
}

internal fun String.oopsSignedNavBarColorKey(): String {
    return this.replace(".", "_P_") + "_" + OopsPrefsKey.KEY_NAV_BAR_COLOR
}