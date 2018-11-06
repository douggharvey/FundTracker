package com.douglasharvey.fundtracker3.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.douglasharvey.fundtracker3.PortfolioFragment

class PortfolioPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = PortfolioFragment.newInstance(position)

override fun getCount(): Int = 3
//todo instead need to pass a list https://github.com/gunhansancar/FragmentPagerExample/blob/master/app/src/main/java/com/gunhansancar/fragmentpagerexample/MyViewPagerAdapter.java

}
