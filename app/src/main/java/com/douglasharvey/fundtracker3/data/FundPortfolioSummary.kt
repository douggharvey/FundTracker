package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo

data class FundPortfolioSummary(
        @ColumnInfo(name = "current_value") val currentValue: Double,
        @ColumnInfo(name = "cost") val cost: Double,
        @ColumnInfo(name = "number_funds") val numberFunds: Double,
        @ColumnInfo(name = "profit_loss") val profitLoss: Double,
        @ColumnInfo(name = "sold_proceeds") val soldProceeds: Double
)

