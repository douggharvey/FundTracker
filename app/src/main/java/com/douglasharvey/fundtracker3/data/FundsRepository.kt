package com.douglasharvey.fundtracker3.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.douglasharvey.fundtracker3.utilities.AppExecutors

class FundsRepository constructor(application: Application) {
    private val fundDao: FundDao
    val allFunds: LiveData<List<FundList>>
    val portfolioList: LiveData<List<FundPortfolioList>>
    val favourites: LiveData<List<FundList>>
    val portfolioSummary: LiveData<FundPortfolioSummary>

    init {
        val db = FundsRoomDatabase.getDatabase(application)
        this.fundDao = db.fundDao()
        this.allFunds = fundDao.getFunds()
        this.favourites = fundDao.getFavourites()
        this.portfolioList = fundDao.getPortfolioList()
        this.portfolioSummary = fundDao.getPortfolioSummary()
    }

/*
    suspend fun getFavourites2(): Deferred<List<String>> =
            coroutineScope {
                async { fundDao.getFavourites2() }
            }
*/

    suspend fun getFavourites2(): List<String> = fundDao.getFavourites2()


    fun insertFund(fund: Fund) {
        AppExecutors.getInstance().diskIO().execute { fundDao.insertFund(fund) }
    }

    fun insertFundPrice(fundPrice: FundPrice) {
        AppExecutors.getInstance().diskIO().execute { fundDao.insertFundPrice(fundPrice) }
    }

    fun bulkInsertFundPrice(fundPriceList: List<FundPrice>) {
        AppExecutors.getInstance().diskIO().execute { fundDao.bulkInsertFundPrice(fundPriceList) }
    }


    fun deleteFavourite(fundCode: String) {
        AppExecutors.getInstance().diskIO().execute { fundDao.deleteFavourite(fundCode) }
    }

    fun insertFavourite(favourite: Favourite) {
        AppExecutors.getInstance().diskIO().execute { fundDao.insertFavourite(favourite) }
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: FundsRepository? = null

        fun getInstance(application: Application) =
                instance ?: synchronized(this) {
                    instance ?: FundsRepository(application).also { instance = it }
                }
    }


}


