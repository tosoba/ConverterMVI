package com.example.there.convertermvi.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.convertermvi.di.vm.ViewModelFactory
import com.example.there.convertermvi.di.vm.ViewModelKey
import com.example.there.convertermvi.presentation.converter.ConverterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PresentationModule {
    @Binds
    @IntoMap
    @ViewModelKey(ConverterViewModel::class)
    abstract fun converterViewModel(viewModel: ConverterViewModel): ViewModel

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}