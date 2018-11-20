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

    @GET("PortfolioValues/api/Funds/1")
    fun funds(): Deferred<ArrayList<FundSource>>
//Şirket Tipi. 1: Menkul Kıymet Yatırım Fonları, 2: Emeklilik Yatırım Fonları, 3: Menkul Kıymet Yatırım Ortaklıkları,
// 4: Tüm Yatırım Fonları, 7: Gayrimenukul Yatırım Ortaklıkları, 8: Risk Sermayesi Yatırım Ortaklıkları.
}
