package com.douglasharvey.fundtracker3.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class FundViewModel(application: Application) : AndroidViewModel(application) {

    val allFunds: LiveData<List<FundList>>
    val favourites: LiveData<List<FundList>>

    init {
        val repository = FundsRepository(application)
        allFunds = repository.allFunds
        favourites = repository.favourites
    }

}
