package io.nichijou.oops

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class OopsActivity : AppCompatActivity() {

    open fun getLayoutInflaterFactory(): LayoutInflaterFactory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Oops.attach(this, getLayoutInflaterFactory())
        super.onCreate(savedInstanceState)
    }

}