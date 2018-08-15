package com.example.there.convertermvi.presentation.converter.di.module

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class TestAppModule {
    @Binds
    abstract fun bindContext(application: Application): Context
}