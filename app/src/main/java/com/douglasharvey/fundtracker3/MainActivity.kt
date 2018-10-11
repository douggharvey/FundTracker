package com.douglasharvey.fundtracker3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.douglasharvey.fundtracker3.api.ServiceGenerator
import com.douglasharvey.fundtracker3.data.Fund
import com.douglasharvey.fundtracker3.data.FundPrice
import com.douglasharvey.fundtracker3.data.FundsRepository
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.Main
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //todo temporarily removed for speed
     //   GlobalScope.launch {readFundList()}
        GlobalScope.launch {readFavouriteValues()}
        //readFavouriteValues()
        Timber.d("readFavouritesValues launched")
    }

    suspend private fun readFavouriteValues() = runBlocking{
        var favouriteList = " "
        val fundInterface = ServiceGenerator.createService(FundInterface::class.java)
        launch(Dispatchers.IO) {
            //todo decide how to handle dates
            val favourites = withContext(Dispatchers.IO) {FundsRepository.getInstance(application).getFavourites2()}
            favouriteList = favourites.joinToString()

            val fundValueArrayList: ArrayList<FundValue> = fundInterface.values(favouriteList, "2018-09-10", "2035-01-01").await()
            for (fundValue: FundValue in fundValueArrayList) {
                val fundPrice = FundPrice(fundValue.fundCode, fundValue.unitValue, fundValue.valueDate)
                Timber.d("INSERT PRICES: "+fundValue.fundCode)
                FundsRepository.getInstance(application).insertFundPrice(fundPrice)
            } //todo consider a transaction here or use bulk insert instead of a loop
        }

    }

    //todo temporary for testing purposes - will be a scheduled job instead
    private fun readFundList() = runBlocking {
        val fundInterface = ServiceGenerator.createService(FundInterface::class.java)
        launch(Dispatchers.Main) {
            val fundSourceArrayList: ArrayList<FundSource> = fundInterface.funds().await() // TODO how to get / handle response code - find other examples
            for (fundSource: FundSource in fundSourceArrayList) {
                val fund = Fund(fundSource.fundCode, fundSource.fundName)
                FundsRepository.getInstance(application).insertFund(fund)
            }
        }
    }

    //todo  what is it for? override fun onSupportNavigateUp() = NavHostFragment.findNavController(my_nav_host_fragment).navigateUp()
}
