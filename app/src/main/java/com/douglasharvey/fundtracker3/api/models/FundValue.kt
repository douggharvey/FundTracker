package com.douglasharvey.fundtracker3

import com.google.gson.annotations.SerializedName

data class FundValue(
        @SerializedName("Tarih")
        val valueDate: String,
        @SerializedName("FonKodu")
        val fundCode: String,
        @SerializedName("BirimPayDegeri")
        val unitValue: Double
)