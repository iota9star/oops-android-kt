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
import io.nichijou.oops.ext.logi
import io.nichijou.oops.widget.NavigationViewTintMode
import kotlinx.android.synthetic.main.activity_primary.*
import kotlinx.android.synthetic.main.activity_primary_content.*

class PrimaryActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary)
        setSupportActionBar(bar)
        initToolbar()
        initTabLayout()
        if (Oops.immed().isFirstTime) {
            Oops.immed().putStaticStatusBarColor<SecondaryActivity>(Color.TRANSPARENT)
            updateTheme(fab)
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
        val snackbarText = randomColor()
        val snackbarAction = randomColor()
        Oops.bulk {
            colorAccent = accent
            colorPrimary = primary
            textColorPrimary = textPrimary
            textColorSecondary = textSecondary
            toolbarActiveColor = active
            toolbarInactiveColor = inactive
            statusBarColor = primary
            windowBackground = if (isDark) Color.BLACK else Color.WHITE
            navBarColor = primary
            snackBarTextColor = snackbarText
            snackBarActionColor = snackbarAction
            snackBarBackgroundColor = randomColor()
            navigationViewMode = NavigationViewTintMode.PRIMARY
            customAttrColorSet(this@PrimaryActivity, R.attr.customColor1, randomColor())
            customAttrColorSet(this@PrimaryActivity, R.attr.customColor2, randomColor())
            rippleView = view
            rippleAnimDuration = 480
        }
        Snackbar.make(coordinatorLayout, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action") {
                logi { "do something..." }
            }.show()
    }

    private fun initTabLayout() {
        tab_layout.setupWithViewPager(view_pager)
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, arrayOf("BaseView", "ListView"), arrayOf(FragmentBaseView.newInstance(), FragmentScrollView.newInstance()))
        tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_menu_send)
        tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_menu_manage)
    }

    private fun initToolbar() {
        toolbar.inflateMenu(R.menu.menu_primary)
        toolbar.menu.findItem(R.id.action_go_next).setOnMenuItemClickListener {
            startActivity(Intent(this, SecondaryActivity::class.java))
            true
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_bottom_nav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
