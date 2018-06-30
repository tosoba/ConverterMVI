package com.example.there.convertermvi.di.module

import com.example.there.convertermvi.presentation.converter.ConverterActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuilderModule {
    @ContributesAndroidInjector
    abstract fun converterActivity(): ConverterActivity
}