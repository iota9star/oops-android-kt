package io.nichijou.oops

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class OopsActivity : AppCompatActivity() {

    open fun getLayoutInflaterFactory(): LayoutInflaterFactory? = null
    open fun getPostProcessor(): PostProcessor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Oops.attach(this, getLayoutInflaterFactory(), getPostProcessor())
        super.onCreate(savedInstanceState)
    }
}