package com.example.there.convertermvi.presentation.converter

import android.app.Activity
import android.app.Application
import android.support.test.InstrumentationRegistry
import com.example.there.convertermvi.presentation.converter.di.DaggerTestAppComponent
import com.example.there.convertermvi.presentation.converter.di.TestAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class TestConverterApp : Application(), HasActivityInjector {
    @Inject
    lateinit var injector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = injector

    private lateinit var appComponent: TestAppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerTestAppComponent.builder().application(this).build()
        appComponent.inject(this)
    }

    companion object {
        fun appComponent(): TestAppComponent =
                (InstrumentationRegistry.getTargetContext().applicationContext as TestConverterApp).appComponent
    }
}