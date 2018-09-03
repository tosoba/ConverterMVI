package com.example.there.convertermvi.data.remote.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ExchangeRatesServiceFactory {
    fun makeExchangeRatesService(isDebug: Boolean): ExchangeRatesService = makeExchangeRatesService(
            okHttpClient = makeOkHttpClient(makeLoggingInterceptor(isDebug)),
            gson = makeGson()
    )

    private fun makeExchangeRatesService(okHttpClient: OkHttpClient, gson: Gson): ExchangeRatesService = Retrofit.Builder()
            .baseUrl("https://api.exchangeratesapi.io/")
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().run { create(ExchangeRatesService::class.java) }


    private fun makeOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

    private fun makeGson(): Gson = GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd")
            .create()

    private fun makeLoggingInterceptor(isDebug: Boolean): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (isDebug)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }
}