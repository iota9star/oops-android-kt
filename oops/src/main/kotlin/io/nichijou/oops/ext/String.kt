package io.nichijou.oops.ext

// eg: ?android:attr/textColorPrimary to OOPS_SIGNED__QM_android_COLON_attr_SLASH_textColorPrimary
internal fun String.oopsSignedAttrName(): String {
    return "OOPS_SIGNED_" + this.replace("?", "_QM_")
            .replace("/", "_SLASH_")
            .replace(":", "_COLON_")
}