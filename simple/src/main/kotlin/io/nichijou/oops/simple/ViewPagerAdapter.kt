package io.nichijou.oops.simple

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(fm: FragmentManager?, private val titles: Array<String>, private val fragments: Array<Fragment>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int) = titles[position]
}