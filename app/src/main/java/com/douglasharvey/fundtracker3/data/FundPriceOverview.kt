package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fund_price_overview")
data class FundPriceOverview(
        @PrimaryKey @ColumnInfo(name = "fund_code") val fundCode: String,
        @ColumnInfo(name = "one_day") val oneDay: Double,
        @ColumnInfo(name = "seven_days") val sevenDays: Double,
        @ColumnInfo(name = "one_month") val oneMonth: Double,
        @ColumnInfo(name = "three_months") val threeMonths: Double,
        @ColumnInfo(name = "six_months") val sixMonths: Double,
        @ColumnInfo(name = "one_year") val oneYear: Double,
        @ColumnInfo(name = "three_years") val threeYears: Double,
        @ColumnInfo(name = "five_years") val fiveYears: Double,
        @ColumnInfo(name = "start_of_year") val startYear: Double,
        @ColumnInfo(name = "last_month") val lastMonth: Double
)