package com.douglasharvey.fundtracker3.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.douglasharvey.fundtracker3.utilities.AppExecutors

class FundsRepository constructor(application: Application) {
    private val fundDao: FundDao
    val allFunds: LiveData<List<FundList>>
    val favourites: LiveData<List<FundList>>

    init {
        val db =  FundsRoomDatabase.getDatabase(application)
        this.fundDao = db.fundDao()
        this.allFunds = fundDao.getFunds()
        this.favourites = fundDao.getFavourites()
    }

    fun insert(fund: Fund) {
        AppExecutors.getInstance().diskIO().execute { fundDao.insert(fund) }
    }

    fun deleteFavourite(fundCode: String) {
        AppExecutors.getInstance().diskIO().execute { fundDao.deleteFavourite(fundCode) }
    }

    fun insertFavourite(favourite: Favourite) {
        AppExecutors.getInstance().diskIO().execute { fundDao.insertFavourite(favourite) }
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: FundsRepository? = null

        fun getInstance(application: Application) =
                instance ?: synchronized(this) {
                    instance ?: FundsRepository(application).also { instance = it }
                }
    }


}


