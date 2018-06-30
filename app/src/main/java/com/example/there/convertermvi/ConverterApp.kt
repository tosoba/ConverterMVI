package com.example.there.convertermvi

import android.app.Activity
import android.app.Application
import com.example.there.convertermvi.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class ConverterApp: Application(), HasActivityInjector {

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector
}