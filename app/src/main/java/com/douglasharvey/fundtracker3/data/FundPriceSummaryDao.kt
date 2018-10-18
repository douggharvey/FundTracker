package com.douglasharvey.fundtracker3.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FundPriceSummaryDao {
    @Query("SELECT * FROM fund_prices ORDER BY fund_code")
    fun getFundValues(): LiveData<List<FundPrice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fundValue: FundPrice): Long

    @Query("DELETE FROM fund_prices WHERE fund_code =:recordId")
    fun delete(recordId: String)

}
