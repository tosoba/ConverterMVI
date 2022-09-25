package com.example.there.convertermvi.data.remote

import com.example.there.convertermvi.data.model.CurrencyExchangeRates
import com.example.there.convertermvi.data.remote.service.ExchangeRatesService
import com.example.there.convertermvi.domain.repository.ICurrencyExchangeRatesDataStore
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

open class CurrencyRemoteDataStore @Inject constructor(
    private val service: ExchangeRatesService
) : ICurrencyExchangeRatesDataStore {
    override fun clearAll(): Completable = throw UnsupportedOperationException()

    override fun insert(cer: CurrencyExchangeRates): Completable =
        throw UnsupportedOperationException()

    override fun get(base: String): Single<CurrencyExchangeRates> =
        service.getExchangeRates(base)
}