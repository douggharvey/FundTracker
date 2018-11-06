package com.douglasharvey.fundtracker3.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FundPortfolioListViewModelFactory(private val application: Application, private val portfolioKey: String) :
        ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FundPortfolioListViewModel(application, portfolioKey) as T
    }
}
