package com.douglasharvey.fundtracker3.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class FundPortfolioListViewModel(application: Application, portfolioKey: String) : AndroidViewModel(application) {

    val portfolioList: LiveData<List<FundSummary>>
    val portfolioSummary: LiveData<FundPortfolioSummary>

    init {
        val repository = FundsRepository(application, portfolioKey)
        portfolioList = repository.portfolioList
        portfolioSummary = repository.portfolioSummary
    }

}
