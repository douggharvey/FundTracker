package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "fund_transaction", primaryKeys = ["fund_code", "transaction_date"],
        foreignKeys = arrayOf(ForeignKey(
                entity = Account::class,
                parentColumns = arrayOf("account_number"),
                childColumns = arrayOf("account") //todo constraint??
        ))
        )
data class FundTransaction(
        @ColumnInfo(name = "fund_code") val fundCode: String,
        @ColumnInfo(name = "fund_price") val fundName: Double,
        @ColumnInfo(name = "unit") val unit: Double,
        @ColumnInfo(name = "transaction_date") val transactionDate: String,
        @ColumnInfo(name = "transaction_type") val transactionType: Char,
        @ColumnInfo(name = "account") val account: String

)

//@Entity(tableName = "account",
//        foreignKeys = arrayOf(ForeignKey(
//                entity = Portfolio::class,
//                parentColumns = arrayOf("portfolio_code"),
//                childColumns = arrayOf("portfolio_code"),
//                onDelete = ForeignKey.CASCADE))) //TODO which constraint? and check whether works as expected
//data class Account(
//        @PrimaryKey @ColumnInfo(name = "account_number") val fundCode: String,
//        @ColumnInfo(name="portfolio_code") val portfolioCode: String,
//        @ColumnInfo(name = "account_name") val fundName: String
//)