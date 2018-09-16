package io.nichijou.oops

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class OopsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Oops.attach(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Oops.resume(this)
    }

    override fun onPause() {
        Oops.pause(this)
        super.onPause()
    }

    override fun onDestroy() {
        Oops.destroy(this)
        super.onDestroy()
    }
}