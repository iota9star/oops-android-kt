package io.nichijou.oops.simple

import io.nichijou.oops.LayoutInflaterFactory
import io.nichijou.oops.OopsActivity

open class BaseActivity : OopsActivity() {
    override fun getLayoutInflaterFactory(): LayoutInflaterFactory? {
        return MyFactory()
    }
}