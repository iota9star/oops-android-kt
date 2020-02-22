package io.nichijou.oops

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class OopsActivity : AppCompatActivity() {
  open fun getViewInflaterFactory(): ViewInflaterFactory? = null
  open fun getViewInflatedProcessor(): ViewInflatedProcessor? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    Oops.attach(this, getViewInflaterFactory(), getViewInflatedProcessor())
    super.onCreate(savedInstanceState)
  }
}
