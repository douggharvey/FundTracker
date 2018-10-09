package com.douglasharvey.fundtracker3

import android.content.Context
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.douglasharvey.fundtracker3.adapters.FundListAdapter
import com.douglasharvey.fundtracker3.data.FundList
import com.douglasharvey.fundtracker3.data.FundViewModel
import kotlinx.android.synthetic.main.fragment_fund_list.view.*
import timber.log.Timber

class FundListFragment : androidx.fragment.app.Fragment(), SearchView.OnQueryTextListener {

    private lateinit var activityContext: Context
    private var allFundList: List<FundList>? = null
    private lateinit var rvFundList: androidx.recyclerview.widget.RecyclerView
    private lateinit var viewManager: androidx.recyclerview.widget.RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewInflater = inflater.inflate(R.layout.fragment_fund_list, container, false)
        activityContext = (this.activity as Context?)!!
        setHasOptionsMenu(true)

        val fundListAdapter = FundListAdapter { fund -> fundItemClicked(fund) }
        rvFundList = viewInflater.rv_fund_list
        viewManager = androidx.recyclerview.widget.LinearLayoutManager(activityContext)

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(activityContext, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL)
        rvFundList.addItemDecoration(dividerItemDecoration)
        rvFundList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = fundListAdapter
        }
        val fundViewModel = ViewModelProviders.of(this).get(FundViewModel::class.java)

        val listType = arguments?.let {
            val safeArgs = FundListFragmentArgs.fromBundle(it)
            safeArgs.listType
        }

        if (listType == "1") {
            fundViewModel.allFunds.observe(this, Observer(fundListAdapter::setFunds))         //todo this partly works, if possible should remove null processing (inside adapter). refer MainActivity in ReceiptTracker
            fundViewModel.allFunds.observe(this, Observer { allFundList = it })
       } else {
            fundViewModel.favourites.observe(this, Observer(fundListAdapter::setFunds))
            fundViewModel.favourites.observe(this, Observer { allFundList = it })
        }
        return viewInflater
    }

    private fun preFetchWebViews() {
        val webViewList: MutableList<FundList> = allFundList!!.toMutableList()
        for (fund2: FundList in webViewList) {
            val view = WebView(context)
            view.loadUrl("http://tefas.gov.tr/FonAnaliz.aspx?FonKod=${fund2.fundCode}")
            Timber.d("prefetching webviews:{fund2.fundCode}")
        }
    }
//todo consider if a viewpager might be better so that webview fragments are retained
    //also check if a vertical viewpager is feasible
    //https://guides.codepath.com/android/viewpager-with-fragmentpageradapter#overview
    private fun fundItemClicked(fund: FundList) {
        //preFetchWebViews()
        //   Toast.makeText(activity, "Clicked: ${fund.fundCode} ${fund.fundName}", Toast.LENGTH_LONG).show()
        val action = FundListFragmentDirections.actionFundListFragmentToWebViewFragment()
        action.setWebAddress("http://tefas.gov.tr/FonAnaliz.aspx?FonKod=${fund.fundCode}")
        Navigation.findNavController(activity!!, R.id.my_nav_host_fragment).navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(this)
        searchView.setQueryHint(getString(R.string.fund_list_search_hint))

        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("not implemented")
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText == null || newText.trim().isEmpty()) { // can be kotlinized
            resetSearch()
            return false
        }

        val filteredFunds: MutableList<FundList>? = allFundList?.toMutableList()
        allFundList?.let {
            for (fund in it) {
                if (!fund.fundCode.toLowerCase().contains(newText.toLowerCase()) &&
                        !fund.fundName.toLowerCase().contains(newText.toLowerCase())) {
                    filteredFunds?.remove(fund)
                }
            }
        }

        assignAdapterList(filteredFunds)
        return false
    }

    fun resetSearch() {
        assignAdapterList(allFundList)
    }

    private fun assignAdapterList(filteredFunds: List<FundList>?) {
        val adapter = FundListAdapter { fund -> fundItemClicked(fund) }
        rvFundList.adapter = adapter
        adapter.setFunds(filteredFunds)
    }

}
