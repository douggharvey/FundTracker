package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

//this Databaseview feature is brand new and only available in alpha version of Room at the moment. Monitor progress on this release.
//Using this method so that fund table can be reloaded whenever necessary with the favourites kept in a separate table.
//the view provides a simple way of getting a join between the two tables to show the favourites easily
@DatabaseView(value="select fund.fund_code, fund.fund_name, ifnull(favourite.fund_code, 0) fav_fund_code " +
        "from fund LEFT OUTER JOIN favourite ON fund.fund_code = favourite.fund_code",
        viewName="fund_list")
data class FundList(
        @ColumnInfo(name = "fund_code") val fundCode: String,
        @ColumnInfo(name = "fund_name") val fundName: String,
        @ColumnInfo(name = "fav_fund_code") val fav_fund_code: String
)
