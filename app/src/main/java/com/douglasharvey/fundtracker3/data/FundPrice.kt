package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "fund_prices", primaryKeys = ["fund_code", "price_date"])
data class FundPrice(
        @ColumnInfo(name = "fund_code") val fundCode: String,
        @ColumnInfo(name = "fund_price") val fundName: String,
        @ColumnInfo(name = "price_date") val priceDate: String
)