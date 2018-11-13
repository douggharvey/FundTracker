package com.douglasharvey.fundtracker3.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.douglasharvey.fundtracker3.PortfolioFragment

class PortfolioPagerAdapter(fm: FragmentManager) : //FragmentPagerAdapter(fm) {
                                                    FragmentStatePagerAdapter(fm) {
    //todo switched back because of a problem with blank pages - find a solution as PagerAdapter is more efficient

    //see https://www.raywenderlich.com/324-viewpager-tutorial-getting-started-in-kotlin
    override fun getItem(position: Int): Fragment = PortfolioFragment.newInstance(position)

    override fun getCount(): Int = 3
    //todo instead need to pass a list https://github.com/gunhansancar/FragmentPagerExample/blob/master/app/src/main/java/com/gunhansancar/fragmentpagerexample/MyViewPagerAdapter.java
    override fun getPageTitle(position: Int): CharSequence? {
        return (when (position) {
            0 -> "Summary: all"
            else -> "Summary: $position"
        })
    }
}

