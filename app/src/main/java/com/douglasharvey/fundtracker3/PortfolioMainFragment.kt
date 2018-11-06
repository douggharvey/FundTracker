package com.douglasharvey.fundtracker3


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.douglasharvey.fundtracker3.adapters.PortfolioPagerAdapter

class PortfolioMainFragment : Fragment() {

    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_portfolio_main, container, false)
        viewPager = view.findViewById(R.id.vp_portfolio) //todo why didn't extensions work here!

        val pagerAdapter = PortfolioPagerAdapter(fragmentManager!!)
        viewPager.adapter = pagerAdapter

        return view;
    }


}
