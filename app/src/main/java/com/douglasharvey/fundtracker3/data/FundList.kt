package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

//this Databaseview feature is brand new and only available in alpha version of Room at the moment. Monitor progress on this release.
//Using this method so that fund table can be reloaded whenever necessary with the favourites kept in a separate table.
//the view provides a simple way of getting a join between the two tables to show the favourites easily
@DatabaseView(value="SELECT fund.fund_code, fund.fund_name, IFNULL(favourite.fund_code, 0) fav_fund_code, " +
        "IFNULL(fund_price,0) fund_price, price_date," +
        "IFNULL(ROUND(po.one_day,2),0) one_day, IFNULL(ROUND(po.seven_days,2),0) seven_days, IFNULL(ROUND(po.one_month,2),0) one_month," +
        "IFNULL(ROUND(po.three_months,2),0) three_months,IFNULL(ROUND(po.six_months,2),0) six_months,IFNULL(ROUND(po.one_year,2),0) one_year,IFNULL(ROUND(po.three_years,2),0) three_years," +
        "IFNULL(ROUND(po.five_years,2),0) five_years,IFNULL(ROUND(po.start_of_year,2),0) start_of_year,IFNULL(ROUND(po.last_month,2),0) last_month " +
        "FROM fund " +
        "LEFT OUTER JOIN favourite ON fund.fund_code = favourite.fund_code " +
        "LEFT OUTER JOIN fund_price_overview po ON fund.fund_code = po.fund_code " +
        "LEFT OUTER JOIN fund_prices ON fund_prices.fund_code = fav_fund_code " +
        "AND fund_prices.price_date = (SELECT MAX(price_date) FROM fund_prices " +
        "WHERE fund_code = fav_fund_code) ",
        viewName="fund_list")
data class FundList(
        @ColumnInfo(name = "fund_code") val fundCode: String,
        @ColumnInfo(name = "fund_name") val fundName: String,
        @ColumnInfo(name = "fav_fund_code") val favFundCode: String,
        @ColumnInfo(name = "fund_price") val fundPrice: String,
        @ColumnInfo(name = "price_date") val priceDate: String? = null, //TODO fund_price IFNULL solution worked but did not work for price_date
        @ColumnInfo(name = "one_day") val oneDay: String,
        @ColumnInfo(name = "seven_days") val sevenDays: String,
        @ColumnInfo(name = "one_month") val oneMonth: String,
        @ColumnInfo(name = "three_months") val threeMonths: String,
        @ColumnInfo(name = "six_months") val sixMonths: String,
        @ColumnInfo(name = "one_year") val oneYear: String,
        @ColumnInfo(name = "three_years") val threeYears: String,
        @ColumnInfo(name = "five_years") val fiveYears: String,
        @ColumnInfo(name = "start_of_year") val startYear: String,
        @ColumnInfo(name = "last_month") val lastMonth: String
)
