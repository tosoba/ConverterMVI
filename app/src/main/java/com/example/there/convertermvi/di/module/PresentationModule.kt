package com.example.there.convertermvi.di.module

import android.arch.lifecycle.ViewModelProvider
import com.example.there.convertermvi.di.vm.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class PresentationModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}