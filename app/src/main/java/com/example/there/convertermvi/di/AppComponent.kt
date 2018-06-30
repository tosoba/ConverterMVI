package com.example.there.convertermvi.di

import android.app.Application
import com.example.there.convertermvi.ConverterApp
import com.example.there.convertermvi.di.module.AppModule
import com.example.there.convertermvi.di.module.BuilderModule
import com.example.there.convertermvi.di.module.DataModule
import com.example.there.convertermvi.di.module.PresentationModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DataModule::class,
    PresentationModule::class,
    BuilderModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: ConverterApp)
}