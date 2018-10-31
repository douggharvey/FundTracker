package com.douglasharvey.fundtracker3.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class FundPortfolioListViewModel(application: Application) : AndroidViewModel(application) {

    val portfolioList: LiveData<List<FundPortfolioList>>
    val portfolioSummary: LiveData<FundPortfolioSummary>

    init {
        val repository = FundsRepository(application)
        portfolioList = repository.portfolioList
        portfolioSummary = repository.portfolioSummary
    }

}
