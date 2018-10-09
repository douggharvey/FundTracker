package com.douglasharvey.fundtracker3

import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import java.util.*

interface FundInterface {
    @GET("PortfolioValues/api/PortfoyDegerleri/TTE,TI3/4/2010-09-11/2018-09-10")
    fun values(): Deferred<ArrayList<FundValue>>

    @GET("PortfolioValues/api/Funds/4")
    fun funds(): Deferred<ArrayList<FundSource>>

}
