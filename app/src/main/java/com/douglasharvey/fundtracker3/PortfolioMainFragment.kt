package com.douglasharvey.fundtracker3


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.douglasharvey.fundtracker3.adapters.PortfolioPagerAdapter
import com.douglasharvey.fundtracker3.data.FundPortfolioMainViewModel


class PortfolioMainFragment : Fragment() {

    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_portfolio_main, container, false)
        viewPager = view.findViewById(R.id.vp_portfolio) //todo why didn't extensions work here! ava.lang.IllegalStateException: vp_portfolio must not be null

        val portfolioList : List<String> = emptyList()
        val pagerAdapter = PortfolioPagerAdapter(fragmentManager!!, context , portfolioList)
        viewPager.adapter = pagerAdapter

        val fundPortfolioMainViewModel = ViewModelProviders.of(this).get(FundPortfolioMainViewModel::class.java)
        fundPortfolioMainViewModel.portfolioNameList.observe(this, Observer { (pagerAdapter:: setPortfolioList)(it)})

        return view;
    }


}
