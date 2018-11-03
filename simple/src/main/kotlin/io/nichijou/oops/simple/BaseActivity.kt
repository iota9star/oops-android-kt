package io.nichijou.oops.simple

import io.nichijou.oops.OopsActivity
import io.nichijou.oops.OopsLayoutInflaterFactory

open class BaseActivity : OopsActivity() {
    override fun getOopsLayoutInflaterFactory(): OopsLayoutInflaterFactory? {
        return MyFactory()
    }
}