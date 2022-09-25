package com.example.there.convertermvi.presentation.converter.di.module

import com.example.there.convertermvi.domain.usecase.impl.GetCurrencyExchangeRates
import dagger.Module
import dagger.Provides
import org.mockito.Mockito.mock
import javax.inject.Singleton

@Module
object TestDomainModule {
    @Provides
    @Singleton
    fun getCurrencyExchangeRates(): GetCurrencyExchangeRates =
        mock(GetCurrencyExchangeRates::class.java)
}