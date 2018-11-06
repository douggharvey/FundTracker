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

    @Query("SELECT fav_fund_code from fund_list WHERE fav_fund_code = fund_code ORDER BY fav_fund_code")
    fun getFavourites2(): List<String>

    @Query("select  fund_code, fund_name, sum(unit) number_units,  current_price, sum(profit_loss) profit_loss, " +
            "sum(current_value) current_value, sum(cost) cost,  sum(sold_proceeds) sold_proceeds" +
            " from fund_portfolio_list " +
            " where portfolio_code = (case when :portfolioCode = 0 then portfolio_code else :portfolioCode end) " +
             " group by fund_code")
    //todo add portfolio parameter here , maybe account too. add group by fund_code to get main page where condition is for portfolio if selected otherwise return all portfolios
    fun getPortfolioList(portfolioCode: Int): LiveData<List<FundSummary>>

    @Query("SELECT sum(current_value) current_value, sum(cost) cost, count(distinct(fund_code)) number_funds, sum(profit_loss) profit_loss, sum(sold_proceeds) sold_proceeds " +
            "from fund_portfolio_list " +
            "where portfolio_code = (case when :portfolioCode = 0 then portfolio_code else :portfolioCode end) ")
    fun getPortfolioSummary(portfolioCode: Int): LiveData<FundPortfolioSummary>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFund(fund: Fund): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFundPrice(fundPrice: FundPrice): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun bulkInsertFundPrice(fundPriceList: List<FundPrice>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavourite(favourite: Favourite)

    @Query("DELETE FROM favourite WHERE fund_code =:fundCode")
    fun deleteFavourite(fundCode: String)

}