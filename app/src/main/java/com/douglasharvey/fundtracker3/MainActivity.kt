package com.douglasharvey.fundtracker3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.douglasharvey.fundtracker3.api.ServiceGenerator
import com.douglasharvey.fundtracker3.data.Fund
import com.douglasharvey.fundtracker3.data.FundsRepository
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //todo temporarily removed for speed
      //  readFundList()
    }

    //todo temporary for testing purposes - will be a scheduled job instead
    private fun readFundList() {
        val fundInterface = ServiceGenerator.createService(FundInterface::class.java)
        GlobalScope.launch(Dispatchers.Main) {
            val fundSourceArrayList: ArrayList<FundSource> = fundInterface.funds().await() // TODO how to get / handle response code - find other examples
            for (fundSource: FundSource in fundSourceArrayList) {
                val fund = Fund(fundSource.fundCode, fundSource.fundName)
                FundsRepository.getInstance(application).insert(fund)
            }
        }
    }

  //todo  what is it for? override fun onSupportNavigateUp() = NavHostFragment.findNavController(my_nav_host_fragment).navigateUp()
}
