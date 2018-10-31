package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "fund_transaction", primaryKeys = ["fund_code", "transaction_date"]) //TODO ADD ACCOUNT??
data class FundTransaction(
        @ColumnInfo(name = "fund_code") val fundCode: String,
        @ColumnInfo(name = "fund_price") val fundName: Double,
        @ColumnInfo(name = "unit") val unit: Double,
        @ColumnInfo(name = "transaction_date") val transactionDate: String,
        @ColumnInfo(name = "transaction_type") val transactionType: Char,
        @ColumnInfo(name = "account") val account: String

)