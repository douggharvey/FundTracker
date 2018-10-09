package com.douglasharvey.fundtracker3.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FundDao {
    @Query("SELECT * from fund_list ORDER BY fund_code")
    fun getFunds(): LiveData<List<FundList>>

    @Query("SELECT * from fund_list WHERE fav_fund_code = fund_code ORDER BY fav_fund_code")
    fun getFavourites(): LiveData<List<FundList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fund: Fund): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavourite(favourite: Favourite)

    @Query("DELETE FROM favourite WHERE fund_code =:fundCode")
    fun deleteFavourite(fundCode: String)

}