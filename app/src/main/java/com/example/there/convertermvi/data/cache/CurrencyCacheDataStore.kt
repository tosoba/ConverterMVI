package com.example.there.convertermvi.data.cache

import com.example.there.convertermvi.data.cache.db.CurrencyExchangeRatesDb
import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import com.example.there.convertermvi.domain.repository.ICurrencyExchangeRatesDataStore
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

open class CurrencyCacheDataStore @Inject constructor(
        currencyExchangeRatesDb: CurrencyExchangeRatesDb
): ICurrencyExchangeRatesDataStore {

    private val exchangeRatesDao = currencyExchangeRatesDb.currencyExchangeRatesDao()

    override fun clearAll(): Completable = Completable.fromCallable {
        exchangeRatesDao.clearAll()
    }

    override fun insert(cer: CurrencyExchangeRates): Completable = Completable.fromCallable {
        exchangeRatesDao.insert(cer)
    }

    override fun get(base: String): Single<CurrencyExchangeRates> = exchangeRatesDao.get(base)
}