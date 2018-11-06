package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo

data class FundSummary(
        @ColumnInfo(name="fund_code") val fundCode: String,
        @ColumnInfo(name="fund_name") val fundName: String,
        @ColumnInfo(name = "number_units") val numberUnits: Double,
        @ColumnInfo(name = "current_price") val currentPrice: Double,
        @ColumnInfo(name = "profit_loss") val profitLoss: Double,
        @ColumnInfo(name = "current_value") val currentValue: Double,
        @ColumnInfo(name = "cost") val cost: Double,
        @ColumnInfo(name = "sold_proceeds") val soldProceeds: Double
)

