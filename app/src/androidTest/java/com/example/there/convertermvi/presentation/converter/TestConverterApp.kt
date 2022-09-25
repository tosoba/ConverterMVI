package com.example.there.convertermvi.presentation.converter

import androidx.test.platform.app.InstrumentationRegistry
import com.example.there.convertermvi.presentation.converter.di.DaggerTestAppComponent
import com.example.there.convertermvi.presentation.converter.di.TestAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TestConverterApp : DaggerApplication() {
    private lateinit var appComponent: TestAppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerTestAppComponent.builder().application(this).build()
        return appComponent
    }

    companion object {
        fun appComponent(): TestAppComponent =
            (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TestConverterApp).appComponent
    }
}