package com.paweldylag.currencyconverter.repository.network

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/**
 * Created by Pawel Dylag
 */
interface RevolutCurrencyRestApi {

    @GET("/latest")
    fun getCurrencyRates(@Query("base") baseCurrency: String): Single<CurrencyRatesResponse>


    companion object {
        fun create() =
                Retrofit.Builder()
                    .baseUrl("https://revolut.duckdns.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build().create(RevolutCurrencyRestApi::class.java)

    }

}

data class CurrencyRatesResponse(
    val base: String,
    val date: Date,
    val rates: Map<String, String>
)