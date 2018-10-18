package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "fund_price_summary", primaryKeys = ["fund_code", "price_date"])
data class FundPriceSummary(
        @ColumnInfo(name = "fund_code") val fundCode: String,
        @ColumnInfo(name = "fund_price") val fundName: Double,
        @ColumnInfo(name = "price_date") val priceDate: String
)