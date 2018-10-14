package io.nichijou.oops

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class OopsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Oops.attach(this)
        super.onCreate(savedInstanceState)
    }
}