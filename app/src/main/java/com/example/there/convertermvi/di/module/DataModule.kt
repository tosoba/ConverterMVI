package com.example.there.convertermvi.di.module

import android.app.Application
import android.arch.persistence.room.Room
import com.example.there.convertermvi.BuildConfig
import com.example.there.convertermvi.data.CurrencyRepository
import com.example.there.convertermvi.data.cache.CurrencyCacheDataStore
import com.example.there.convertermvi.data.cache.db.CurrencyExchangeRatesDb
import com.example.there.convertermvi.data.remote.CurrencyRemoteDataStore
import com.example.there.convertermvi.data.remote.service.ExchangeRatesService
import com.example.there.convertermvi.data.remote.service.ExchangeRatesServiceFactory
import com.example.there.convertermvi.domain.repository.ICurrencyExchangeRatesDataStore
import com.example.there.convertermvi.domain.repository.ICurrencyExchangeRatesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DataModule {

    @Binds
    abstract fun remoteDataStore(remoteDataStore: CurrencyRemoteDataStore): ICurrencyExchangeRatesDataStore

    @Binds
    abstract fun cacheDataStore(cacheDataStore: CurrencyCacheDataStore): ICurrencyExchangeRatesDataStore

    @Binds
    abstract fun currencyRepository(repository: CurrencyRepository): ICurrencyExchangeRatesRepository

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun exchangeRatesService(): ExchangeRatesService = ExchangeRatesServiceFactory.makeExchangeRatesService(BuildConfig.DEBUG)

        @Provides
        @JvmStatic
        fun currencyExchangeRatesDatabase(application: Application): CurrencyExchangeRatesDb = Room.databaseBuilder(
                application.applicationContext,
                CurrencyExchangeRatesDb::class.java,
                "cer.db"
        ).build()
    }
}