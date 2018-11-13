package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "fund_transaction",
        foreignKeys = arrayOf(ForeignKey(
                entity = Account::class,
                parentColumns = arrayOf("account_number"),
                childColumns = arrayOf("account") //todo constraint??
        ))
        )
data class FundTransaction(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "sequence") val sequence: Int,
        @ColumnInfo(name = "fund_code") val fundCode: String,
        @ColumnInfo(name = "fund_price") val fundName: Double,
        @ColumnInfo(name = "unit") val unit: Double,
        @ColumnInfo(name = "transaction_date") val transactionDate: String,
        @ColumnInfo(name = "transaction_type") val transactionType: Char,
        @ColumnInfo(name = "account") val account: String
)
