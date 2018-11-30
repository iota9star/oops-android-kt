package io.nichijou.oops.simple

import io.nichijou.oops.OopsActivity
import io.nichijou.oops.PostProcessor
import io.nichijou.oops.font.OopsFontPostProcessor

open class BaseActivity : OopsActivity() {
    override fun getLayoutInflaterFactory() = MyFactory()
    override fun getPostProcessor(): PostProcessor? = OopsFontPostProcessor()
}