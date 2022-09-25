package com.example.there.convertermvi.presentation.converter.di

import android.app.Application
import com.example.there.convertermvi.ConverterApp
import com.example.there.convertermvi.di.module.BuilderModule
import com.example.there.convertermvi.di.module.DataModule
import com.example.there.convertermvi.di.module.PresentationModule
import com.example.there.convertermvi.domain.usecase.impl.GetCurrencyExchangeRates
import com.example.there.convertermvi.presentation.converter.TestConverterApp
import com.example.there.convertermvi.presentation.converter.di.module.TestAppModule
import com.example.there.convertermvi.presentation.converter.di.module.TestDomainModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        TestAppModule::class,
        DataModule::class,
        TestDomainModule::class,
        PresentationModule::class,
        BuilderModule::class
    ]
)
interface TestAppComponent: AndroidInjector<TestConverterApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestAppComponent
    }

    fun getCurrencyExchangeRates(): GetCurrencyExchangeRates
}