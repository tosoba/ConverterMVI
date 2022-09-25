package com.example.there.convertermvi.di

import android.app.Application
import com.example.there.convertermvi.ConverterApp
import com.example.there.convertermvi.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        DataModule::class,
        DomainModule::class,
        PresentationModule::class,
        BuilderModule::class
    ]
)
interface AppComponent : AndroidInjector<ConverterApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}