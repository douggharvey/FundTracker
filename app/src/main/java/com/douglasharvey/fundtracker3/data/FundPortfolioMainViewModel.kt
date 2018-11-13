package com.douglasharvey.fundtracker3.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class FundPortfolioMainViewModel(application: Application) : AndroidViewModel(application) {

    val portfolioNameList: LiveData<List<String>>

    init {
        val repository = FundsRepository(application)
        portfolioNameList = repository.portfolioNameList
    }

}
