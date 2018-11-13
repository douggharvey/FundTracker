package com.douglasharvey.fundtracker3.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//https://futurestud.io/tutorials/retrofit-2-creating-a-sustainable-android-client

object ServiceGenerator {

    private val BASE_URL = "https://ws.spk.gov.tr"

    private val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())

    private var retrofit = builder.build()

    private val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val httpClient = OkHttpClient.Builder()

    fun <S> createService(
            serviceClass: Class<S>): S {
        if (!httpClient.interceptors().contains(logging)) {
          //  httpClient.addInterceptor(logging) //TODO why did interceptor become null??
            builder.client(httpClient.build())
            retrofit = builder.build()
        }

        return retrofit.create(serviceClass)
    }
}