package com.douglasharvey.fundtracker3


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.douglasharvey.fundtracker3.adapters.FundPortfolioListAdapter
import com.douglasharvey.fundtracker3.application.FundTrackerApplication
import com.douglasharvey.fundtracker3.data.FundPortfolioListViewModel
import com.douglasharvey.fundtracker3.data.FundPortfolioListViewModelFactory
import com.douglasharvey.fundtracker3.data.FundPortfolioSummary
import com.douglasharvey.fundtracker3.data.FundSummary
import kotlinx.android.synthetic.main.fragment_portfolio.*
import kotlinx.android.synthetic.main.fragment_portfolio.view.*


class PortfolioFragment : androidx.fragment.app.Fragment() {

    private lateinit var activityContext: Context
    private lateinit var rvFundPortfolioList: androidx.recyclerview.widget.RecyclerView
    private lateinit var viewManager: androidx.recyclerview.widget.RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewInflater = inflater.inflate(R.layout.fragment_portfolio, container, false)
        activityContext = (this.activity as Context?)!!
        setHasOptionsMenu(true)

        val FundPortfolioListAdapter = FundPortfolioListAdapter { fund -> fundItemClicked(fund) }
        rvFundPortfolioList = viewInflater.rv_fund_portfolio_list
        viewManager = androidx.recyclerview.widget.LinearLayoutManager(activityContext)

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(activityContext, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL)
        rvFundPortfolioList.addItemDecoration(dividerItemDecoration)
        rvFundPortfolioList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = FundPortfolioListAdapter
        }
        val portfolioKey = "1" //A=ALL
        val fundViewModel = ViewModelProviders.of(this,
                FundPortfolioListViewModelFactory(FundTrackerApplication.instance, portfolioKey)).
                get(portfolioKey, FundPortfolioListViewModel::class.java)

//todo finish portfolio handling, need 1 screen with all portfolio's total, then 1 screen per portfolio
        //also do summary at top of screen
        //need viewpager
        //decide how to implement adding a new portfolio

        fundViewModel.portfolioList.observe(this, Observer(FundPortfolioListAdapter::setFundPortfolioList))
        fundViewModel.portfolioSummary.observe(this, Observer<FundPortfolioSummary> { portfolioSummary ->
            setFundPortfolioSummary(portfolioSummary)
        })
        return viewInflater
    }

    private fun setFundPortfolioSummary(portfolioSummary: FundPortfolioSummary) {
        tv_total_amount.text = "%.2f".format(portfolioSummary.currentValue)
        tv_cost.text = "%.2f".format(portfolioSummary.cost)
        tv_sold_proceeds.text = "%.2f".format(portfolioSummary.soldProceeds)
        tv_profit_loss.text = "%.2f".format(portfolioSummary.profitLoss)
        tv_fund_number.text = "%.0f".format(portfolioSummary.numberFunds)
    }

    private fun fundItemClicked(fund: FundSummary) {
//todo
    }


}
