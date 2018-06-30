package com.example.there.convertermvi.domain.repository

import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import io.reactivex.Completable
import io.reactivex.Single

interface ICurrencyExchangeRatesDataStore {
    fun clearAll(): Completable

    fun insert(cer: CurrencyExchangeRates): Completable

    fun get(base: String): Single<CurrencyExchangeRates>
}