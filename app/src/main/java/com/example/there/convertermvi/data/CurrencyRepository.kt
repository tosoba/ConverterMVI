package com.example.there.convertermvi.data

import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import com.example.there.convertermvi.di.Dependencies
import com.example.there.convertermvi.domain.repository.ICurrencyExchangeRatesDataStore
import com.example.there.convertermvi.domain.repository.ICurrencyExchangeRatesRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

open class CurrencyRepository @Inject constructor(
    @Named(Dependencies.remoteDataStore)
    private val remoteDataStore: ICurrencyExchangeRatesDataStore,

    @Named(Dependencies.cacheDataStore)
    private val cacheDataStore: ICurrencyExchangeRatesDataStore
) : ICurrencyExchangeRatesRepository {
    override fun clearAll(): Completable = cacheDataStore.clearAll()

    override fun insert(cer: CurrencyExchangeRates): Completable = cacheDataStore.insert(cer)

    override fun get(base: String): Single<CurrencyExchangeRates> {
        val remoteSource = remoteDataStore.get(base)
            .flatMap { insert(it).toSingle { it } }
        return cacheDataStore.get(base)
            .flatMap {
                if (it.isValid) Single.just(it)
                else remoteSource
            }
            .onErrorResumeNext(remoteSource)
    }
}