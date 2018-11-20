package com.douglasharvey.fundtracker3

import com.google.gson.annotations.SerializedName

data class FundSource(
        @SerializedName("Kodu")
        val fundCode: String,
        @SerializedName("Adi")
        val fundName: String,
        @SerializedName("Tipi")
        val fundType: String
)