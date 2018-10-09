package com.douglasharvey.fundtracker3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fund")
data class Fund(
        @PrimaryKey @ColumnInfo(name = "fund_code") val fundCode: String,
        @ColumnInfo(name = "fund_name") val fundName: String
//        @ColumnInfo(name = "favourite", index = true) val favourite: kotlin.Int // TODO index on favourites
)