package io.nichijou.oops.simple

import io.nichijou.oops.OopsActivity
import io.nichijou.oops.ViewInflaterFactory

open class BaseActivity : OopsActivity() {
  override fun getViewInflaterFactory(): ViewInflaterFactory? = MyFactory()
}
