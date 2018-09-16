package io.nichijou.oops.simple

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.GlidePalette
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsActivity
import io.nichijou.oops.widget.BottomNavigationViewBackgroundMode
import io.nichijou.oops.widget.BottomNavigationViewIconTextMode
import kotlinx.android.synthetic.main.activity_secondary.*
import java.util.*

class FourthActivity : OopsActivity() {
    private val url = arrayOf(
            "https://images.unsplash.com/photo-1533376674118-685f85d75f84?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=690ef7ffcef263c7533bf5e145b33259&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1533374900843-76bbd8249901?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=fff6e479b20fe01e1c60e36969bd4d06&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1533362928417-3311df1f95a0?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=d8e1e3d9055fe000c6950113974bba9f&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1533219346979-3030e2810f5b?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=f5b8ada24d80323fbc09b1ba44e601a3&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1533224081996-0a96a8481e89?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=7df7f991477351a1ac1122ef5f58493b&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1533409268457-200efaaa28f0?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=ccdfc4e8ce1761661da71764377b2c05&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1533409268457-200efaaa28f0?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=ccdfc4e8ce1761661da71764377b2c05&auto=format&fit=crop&w=500&q=60",
            "https://images.unsplash.com/photo-1533309999017-7d5a3f6d3ccf?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=f9549ca8529ef957a0bdce6191d7aeff&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533309999017-7d5a3f6d3ccf?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=f9549ca8529ef957a0bdce6191d7aeff&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533293046890-f1ab3e5e3af9?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=6881a99773ca42bda1bd724e6b388000&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533225248287-b8d7346828db?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=dd6ac3ee4d3078e3b5415f9ef644702d&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533157976639-d31283e8bf55?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=935ee7d27d523efad3d97e5250d2cacc&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533225737818-2a9101586b15?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=1ea44910ab9182e29f6966f9aa94c726&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533146759402-744aed2b518a?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=5306b6cc83e948b771d49da7f7c6378d&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533265399983-d8af79e01427?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=05b115929adb415e5b6fdcc69bf74648&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533247774066-59cbfc352299?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=c7dbc9b4288ec91efea7c85cd0df45db&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533245938827-b2a72694a1a8?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=dd40c98cfdfa1cf5993902e4fef6083e&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533235171920-57783cd98f09?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=5e883299d9954a1e762d84fca8fc778b&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533232117614-42803fbf6c45?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=8e4a7643df2b34c08c432a591db180a6&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533227356842-2b94d2d24d8d?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=2e2bb407d678cab8d44ad3144521e4bd&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533201197546-fd2cf3de04d4?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=2a9b9eaa9e9c0408b8da1beb0fdc1e88&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533176161771-e15728729aa1?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=2d94313313cd301097f2920b7a127a0e&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533158165527-63796a27c752?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=72d39c51a33b12e984243eec4a51b264&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533153120331-1561eb33191d?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=348000d595838e1a9638eeffd16fc207&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533144188434-eb0442504392?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=a9f7636438e9b56eab8b28995f8aae4b&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533201544172-7aa45de70bc8?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=3dc5b6b32f1e20bf03e9b2c4d95e582b&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533155870708-1fd99390f81b?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=435d6faaa5bba4e78215838c94e1a8e2&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533172634255-be2cc549c7e3?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=309624410dc3094f0527f1105bb968d6&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533162507191-d90c625b2640?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=78e6cfb0fd1c5aa17dfdf6e41f1c3451&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533156841469-176ae22a4419?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=96c729f112137240d1b5c0f3e6c10d17&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533153309598-39dd04d03af2?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=762be954da945ab02dc032fa36850d9c&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533121287637-2a25efaeac79?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=1cb87c4d0fdebe3d803eaa0142698337&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533143905186-1d7b05a6cd53?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=4aa2c6fe5cd5178f86216c5e1d149107&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533141947706-226ac3586b0a?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=62cfb92bd22d6e1f80062e04e41dac6e&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533130061792-64b345e4a833?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=f704f09189904f9e8b4098df26c3bbb2&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533144882770-613c81e5c711?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=0c4f14495dd43b27c72e75810066a67b&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533131421469-6eb2d7b791f4?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=801eb80dae49c96ee8d44985cad5a763&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533127523523-3b07168462c2?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=099f8b02a8155913f99cd2d08cb0be62&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533116487219-9816024a82d5?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=dbcf11ce3859dafcd5d6e093e014549c&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533047197222-2e920d139da5?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=18d670fe665669c71f872be5e43b4638&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533046909410-8e8913bf28eb?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=a47034fcb0a8c89f366ad3dd9777c169&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533108220846-8a42207cbc90?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=8f64836726591de05bc98c5004d526a2&auto=format&fit=crop&w=400&q=60",
            "https://images.unsplash.com/photo-1533049949835-4716d79938e3?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=31725571e6738deb5154df9b4e08b737&auto=format&fit=crop&w=400&q=60"
    )
    private val bgArr = arrayOf(BottomNavigationViewBackgroundMode.AUTO, BottomNavigationViewBackgroundMode.ACCENT, BottomNavigationViewBackgroundMode.PRIMARY, BottomNavigationViewBackgroundMode.PRIMARY_DARK)
    private val itArr = arrayOf(BottomNavigationViewIconTextMode.AUTO, BottomNavigationViewIconTextMode.ACCENT, BottomNavigationViewIconTextMode.PRIMARY)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondary)
        collapsingToolbar.title = "Fourth"
        toolbar.inflateMenu(R.menu.menu_secondary)
        toolbar.menu.findItem(R.id.action_change_image).setOnMenuItemClickListener {
            loadImage()
            true
        }
        val classes = arrayOf(SecondaryActivity::class.java, ThirdActivity::class.java, FourthActivity::class.java, FifthActivity::class.java)
        toolbar.menu.findItem(R.id.action_next).setOnMenuItemClickListener {
            val intent = Intent(this@FourthActivity, classes[Random().nextInt(classes.size)])
            startActivity(intent)
            true
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
        fab.setOnClickListener {
            changeTheme(it)
            loadImage()
        }
        loadImage()
    }

    private fun changeTheme(view: View) {
        val primary = randomColor()
        Oops.oops {
            theme = if (theme == R.style.AppTheme) R.style.AppThemeDark else R.style.AppTheme
            colorAccent = randomColor()
            colorPrimary = primary
            statusBarColor = primary
            iconTitleActiveColor = randomColor()
            bottomNavigationViewBackgroundMode = bgArr[Random().nextInt(bgArr.size)]
            bottomNavigationViewIconTextMode = itArr[Random().nextInt(itArr.size)]
            swipeRefreshLayoutBackgroundColor = randomColor()
            isDark = false
            rippleAnimDuration = 480
            rippleView = view
        }
    }

    private fun loadImage() {
        val u = url[Random().nextInt(url.size)]
        Glide.with(this)
                .load(u)
                .listener(GlidePalette.with(u)
                        .intoCallBack { p ->
                            p?.swatches?.forEach {
                                Oops.oops.collapsingToolbarColor = it.rgb
                                return@forEach
                            }
                        }
                )
                .into(image_view)
    }
}
