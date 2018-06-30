package com.example.there.convertermvi.data.remote.service

import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRatesService {
    @GET("latest")
    fun getExchangeRates(@Query("base") base: String): Single<CurrencyExchangeRates>
}