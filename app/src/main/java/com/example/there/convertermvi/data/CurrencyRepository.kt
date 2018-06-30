package com.example.there.convertermvi.data

import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import com.example.there.convertermvi.domain.repository.ICurrencyExchangeRatesDataStore
import com.example.there.convertermvi.domain.repository.ICurrencyExchangeRatesRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.io.InvalidObjectException
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
        private val remoteDataStore: ICurrencyExchangeRatesDataStore,
        private val cacheDataStore: ICurrencyExchangeRatesDataStore
) : ICurrencyExchangeRatesRepository {

    override fun clearAll(): Completable = cacheDataStore.clearAll()

    override fun insert(cer: CurrencyExchangeRates): Completable = cacheDataStore.insert(cer)

    override fun get(base: String): Single<CurrencyExchangeRates> = cacheDataStore.get(base)
            .flatMap {
                if (it.isValid) Single.just(it)
                else Single.error { InvalidObjectException("Cached value is not valid (outdated).") }
            }
            .onErrorResumeNext(remoteDataStore.get(base))
}