package com.douglasharvey.fundtracker3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.douglasharvey.fundtracker3.api.ServiceGenerator
import com.douglasharvey.fundtracker3.data.Fund
import com.douglasharvey.fundtracker3.data.FundPrice
import com.douglasharvey.fundtracker3.data.FundsRepository
import com.douglasharvey.fundtracker3.utilities.NetworkUtils
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            //todo temporarily removed for speed
            Toast.makeText(applicationContext, "Fund List & values updating", Toast.LENGTH_LONG).show()
            GlobalScope.launch { readFundList() } //TODO LOGGING INTERCEPTOR NULL PROBLEM
            GlobalScope.launch { readFavouriteValues() }
        }
    }

    suspend private fun readFavouriteValues() = runBlocking {
        var favouriteList = " "
        val fundInterface = ServiceGenerator.createService(FundInterface::class.java)
        launch(Dispatchers.IO) {
            //todo decide how to handle dates
            val favourites = withContext(Dispatchers.IO) { FundsRepository.getInstance(application).getFavourites2() }
            if (favourites.size != 0) {
                favouriteList = favourites.joinToString()

                val fundValueArrayList: ArrayList<FundValue> = fundInterface.values(favouriteList, "2017-10-01", "2035-01-01").await()
                val fundPriceList: ArrayList<FundPrice> = arrayListOf()

                for (fundValue: FundValue in fundValueArrayList) {
                    fundPriceList.add(FundPrice(fundValue.fundCode, fundValue.unitValue, fundValue.valueDate))
                }

                FundsRepository.getInstance(application).bulkInsertFundPrice(fundPriceList)
                withContext(Dispatchers.Main) { Toast.makeText(applicationContext, "Values updated", Toast.LENGTH_LONG).show() }
            }

        }

    }

    //todo temporary for testing purposes - will be a scheduled job instead
    suspend private fun readFundList() = runBlocking {
        val fundInterface = ServiceGenerator.createService(FundInterface::class.java)
        launch(Dispatchers.Main) {
            val fundSourceArrayList: ArrayList<FundSource> = fundInterface.funds().await() // TODO how to get / handle response code - find other examples
            for (fundSource: FundSource in fundSourceArrayList) {
                val fund = Fund(fundSource.fundCode, fundSource.fundName)
                FundsRepository.getInstance(application).insertFund(fund)
            }
            withContext(Dispatchers.Main) { Toast.makeText(applicationContext, "Fund List updated", Toast.LENGTH_LONG).show() }
        }
    }

    //todo  what is it for? override fun onSupportNavigateUp() = NavHostFragment.findNavController(my_nav_host_fragment).navigateUp()
}
