package io.nichijou.oops.simple

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import io.nichijou.oops.Oops
import io.nichijou.oops.OopsActivity
import io.nichijou.oops.ext.logi
import io.nichijou.oops.widget.NavigationViewTintMode
import kotlinx.android.synthetic.main.activity_primary.*
import kotlinx.android.synthetic.main.activity_primary_content.*

class PrimaryActivity : OopsActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary)
        initToolbar()
        initTabLayout()
        if (Oops.oops.isFirstTime) {
//            updateTheme(fab)
        }
        fab.setOnClickListener { view ->
            updateTheme(view)
        }
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun updateTheme(view: View) {
        val accent = randomColor()
        val primary = randomColor()
        val textPrimary = randomColor()
        val textSecondary = randomColor()
        val active = randomColor()
        val inactive = randomColor()
        val windowColor = Color.WHITE
        val snackbarText = randomColor()
        val snackbarAction = randomColor()
        val cardColor = randomColor()
        Oops.oops {
            colorAccent = accent
            colorPrimary = primary
            textColorPrimary = textPrimary
            textColorSecondary = textSecondary
            iconTitleActiveColor = active
            iconTitleInactiveColor = inactive
            statusBarColor = primary
            navBarColor = primary
            windowBackground = windowColor
            snackBarTextColor = snackbarText
            snackBarActionColor = snackbarAction
            snackBarBackgroundColor = randomColor()
            cardViewBackgroundColor = cardColor
            isDark = false
            navigationViewMode = NavigationViewTintMode.PRIMARY
            rippleView = view
            rippleAnimDuration = 480
        }
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action") {
                    logi { "do something..." }
                }.show()
    }

    private fun initTabLayout() {
        tab_layout.setupWithViewPager(view_pager)
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, arrayOf("BaseView", "ListView"), arrayOf(FragmentBaseView.newInstance(), FragmentListView.newInstance()))
        tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_menu_send)
        tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_menu_manage)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_primary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_go_next -> {
                startActivity(Intent(this, SecondaryActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
