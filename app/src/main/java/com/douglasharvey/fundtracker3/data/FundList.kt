package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

//this Databaseview feature is brand new and only available in alpha version of Room at the moment. Monitor progress on this release.
//Using this method so that fund table can be reloaded whenever necessary with the favourites kept in a separate table.
//the view provides a simple way of getting a join between the two tables to show the favourites easily
@DatabaseView(value="SELECT fund.fund_code, fund.fund_name, IFNULL(favourite.fund_code, 0) fav_fund_code, " +
        "IFNULL(fund_price,0) fund_price, price_date FROM fund " +
        "LEFT OUTER JOIN favourite ON fund.fund_code = favourite.fund_code " +
        "LEFT OUTER JOIN fund_prices ON fund_prices.fund_code = fav_fund_code " +
        "AND fund_prices.price_date = (SELECT MAX(price_date) FROM fund_prices " +
        "WHERE fund_code = fav_fund_code) ",
        viewName="fund_list")
data class FundList(
        @ColumnInfo(name = "fund_code") val fundCode: String,
        @ColumnInfo(name = "fund_name") val fundName: String,
        @ColumnInfo(name = "fav_fund_code") val favFundCode: String,
        @ColumnInfo(name = "fund_price") val fundPrice: String,
        @ColumnInfo(name = "price_date") val priceDate: String? = null //TODO fund_price IFNULL solution worked but did not work for price_date
)

//select fund_prices.fund_price, fund_prices.price_date, fund.fund_code, fund.fund_name, ifnull(favourite.fund_code, 0) fav_fund_code from fund
// LEFT OUTER JOIN favourite ON fund.fund_code = favourite.fund_code
// LEFT OUTER JOIN fund_prices ON fund_prices.fund_code = fav_fund_code
// and fund_prices.price_date = (select max(price_date) from fund_prices
// where fund_code = fav_fund_code) WHERE fav_fund_code = fund.fund_code