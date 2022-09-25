package com.example.there.convertermvi

import com.example.there.convertermvi.di.AppInjector
import com.example.there.convertermvi.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class ConverterApp : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
       return DaggerAppComponent
           .builder()
           .application(this)
           .build()
    }
}