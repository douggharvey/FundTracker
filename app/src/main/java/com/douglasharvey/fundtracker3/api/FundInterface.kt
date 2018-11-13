package com.douglasharvey.fundtracker3.api

import com.douglasharvey.fundtracker3.FundSource
import com.douglasharvey.fundtracker3.FundValue
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface FundInterface {
    @GET("PortfolioValues/api/PortfoyDegerleri/{favourites}/4/{startDate}/{endDate}")
    fun values(@Path("favourites") favouriteList: String,
               @Path( "startDate") startDate: String,
               @Path( "endDate") endDate: String
    ): Deferred<ArrayList<FundValue>>

    @GET("PortfolioValues/api/Funds/4")
    fun funds(): Deferred<ArrayList<FundSource>>

}
