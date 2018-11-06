package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "account",
        foreignKeys = arrayOf(ForeignKey(
                entity = Portfolio::class,
                parentColumns = arrayOf("portfolio_code"),
                childColumns = arrayOf("portfolio_code"),
                onDelete = ForeignKey.CASCADE))) //TODO which constraint? and check whether works as expected
data class Account(
        @PrimaryKey @ColumnInfo(name = "account_number") val fundCode: String,
        @ColumnInfo(name="portfolio_code") val portfolioCode: Int,
        @ColumnInfo(name = "account_name") val fundName: String
)