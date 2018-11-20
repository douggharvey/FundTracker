package com.douglasharvey.fundtracker3

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.douglasharvey.fundtracker3.api.FundInterface
import com.douglasharvey.fundtracker3.api.ServiceGenerator
import com.douglasharvey.fundtracker3.application.prefs
import com.douglasharvey.fundtracker3.data.Fund
import com.douglasharvey.fundtracker3.data.FundPrice
import com.douglasharvey.fundtracker3.data.FundsRepository
import com.douglasharvey.fundtracker3.utilities.NetworkUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            //todo temporarily removed for speed

            val sevenDaysAgo = addDays(Date(System.currentTimeMillis()), -7)
            if (prefs.namesUpdateDate < sevenDaysAgo) {
                Snackbar.make(mainLayout, getString(R.string.fund_list_updating), Snackbar.LENGTH_LONG).show();
                GlobalScope.launch { readFundList() }
            } //TODO LOGGING INTERCEPTOR NULL PROBLEM

//todo if a new favourite is added should reset price date or do a special load just of that favourite's prices
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val priceDatePlus1 = addDays(formatter.parse(prefs.pricesDate), 1)
            if (priceDatePlus1 < Date(System.currentTimeMillis())) {
                Snackbar.make(mainLayout, getString(R.string.values_updating), Snackbar.LENGTH_LONG).show();
                GlobalScope.launch { readFavouriteValues() }
            }
        }
    }

    fun addDays(date: Date, days: Int): Date {
        val cal = GregorianCalendar()
        cal.time = date
        cal.add(Calendar.DATE, days)
        return cal.time
    }

    //todo temporary for testing purposes - will be a scheduled job instead
    suspend private fun readFundList() = runBlocking {
        prefs.namesUpdateDate = Date(System.currentTimeMillis())
        val fundInterface = ServiceGenerator.createService(FundInterface::class.java)
        launch(Dispatchers.Main) {
            val fundSourceArrayList: ArrayList<FundSource> = fundInterface.funds().await() // TODO how to get / handle response code - find other examples
            for (fundSource: FundSource in fundSourceArrayList) {
                val fund = Fund(fundSource.fundCode, fundSource.fundName)
                FundsRepository.getInstance(application).insertFund(fund)
            }
            withContext(Dispatchers.Main) { Snackbar.make(mainLayout, getString(R.string.fund_list_updated), Snackbar.LENGTH_LONG).show(); }
        }
    }

    suspend private fun readFavouriteValues() = runBlocking {
        var favouriteList = " "
        val fundInterface = ServiceGenerator.createService(FundInterface::class.java)
        launch(Dispatchers.IO) {
            val favourites = withContext(Dispatchers.IO) { FundsRepository.getInstance(application).getFavourites2() }
            if (favourites.size != 0) {
                favouriteList = favourites.joinToString()

                val fundValueArrayList: ArrayList<FundValue> = fundInterface.values(favouriteList, prefs.pricesDate, "2035-01-01").await()
                val fundPriceList: ArrayList<FundPrice> = arrayListOf()
                var valueDate: String
                for (fundValue: FundValue in fundValueArrayList) {
                    fundPriceList.add(FundPrice(fundValue.fundCode, fundValue.unitValue, fundValue.valueDate))
                    valueDate = fundValue.valueDate.substring(0, 10)
                    if (valueDate > prefs.pricesDate) {
                        prefs.pricesDate = valueDate
                    }
                }

                FundsRepository.getInstance(application).bulkInsertFundPrice(fundPriceList)
                withContext(Dispatchers.Main) { Snackbar.make(mainLayout, getString(R.string.values_updated), Snackbar.LENGTH_LONG).show(); }
            }

        }

    }


    //todo  what is it for? override fun onSupportNavigateUp() = NavHostFragment.findNavController(my_nav_host_fragment).navigateUp()
}
