package io.nichijou.oops.simple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.nichijou.oops.Oops

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Oops.attach(this, MyFactory())
        super.onCreate(savedInstanceState)
    }
}