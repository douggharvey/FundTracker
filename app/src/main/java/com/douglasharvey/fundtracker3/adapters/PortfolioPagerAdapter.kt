package com.douglasharvey.fundtracker3.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.douglasharvey.fundtracker3.PortfolioFragment
import com.douglasharvey.fundtracker3.R

class PortfolioPagerAdapter(fm: FragmentManager,
                            val context: Context?) : //FragmentPagerAdapter(fm) {
                                                    FragmentStatePagerAdapter(fm) {
    //todo switched back because of a problem with blank pages - find a solution as PagerAdapter is more efficient

    var portfolioNames: List<String> = emptyList()
    //see https://www.raywenderlich.com/324-viewpager-tutorial-getting-started-in-kotlin
    override fun getItem(position: Int): Fragment = PortfolioFragment.newInstance(position)

    override fun getCount(): Int = portfolioNames.size + 1

    override fun getPageTitle(position: Int): CharSequence? {
        val summaryText = context!!.getString(R.string.summary_text)
        val summaryAll = context.getString(R.string.summary_all)
        return (when (position) {
            0 -> "$summaryText $summaryAll"
            else -> {
                if (portfolioNames.size > 0 ) {
                    val portfolioName = portfolioNames.get(position - 1)
                    "$summaryText $portfolioName"
                }
                else {
                    "$summaryText $position"
                }
            }
        })
    }

    fun setPortfolioList(portfolioNames: List<String>) {
        this.portfolioNames = portfolioNames
        notifyDataSetChanged()
    }
}

