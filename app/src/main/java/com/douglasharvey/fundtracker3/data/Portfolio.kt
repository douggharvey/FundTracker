package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio")
data class Portfolio(
        @PrimaryKey @ColumnInfo(name = "portfolio_code") val portfolioCode: String,
        @ColumnInfo(name = "portfolio_name") val fundName: String
)