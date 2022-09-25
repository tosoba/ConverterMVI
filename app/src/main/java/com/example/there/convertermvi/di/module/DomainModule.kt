package com.example.there.convertermvi.di.module

import com.example.there.convertermvi.domain.repository.ICurrencyExchangeRatesRepository
import com.example.there.convertermvi.domain.usecase.impl.GetCurrencyExchangeRates
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class DomainModule {
    @Provides
    fun getCurrencyExchangeRates(repository: ICurrencyExchangeRatesRepository): GetCurrencyExchangeRates =
        GetCurrencyExchangeRates(
            repository = repository,
            threadScheduler = Schedulers.io(),
            postThreadScheduler = AndroidSchedulers.mainThread()
        )
}