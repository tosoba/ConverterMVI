package com.example.there.convertermvi.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.there.convertermvi.ConverterApp
import com.example.there.convertermvi.util.registerFragmentLifecycleCallbacks
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

object AppInjector {
    private val activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = handleActivity(activity)

        override fun onActivityStarted(activity: Activity) = Unit

        override fun onActivityResumed(activity: Activity) = Unit

        override fun onActivityPaused(activity: Activity) = Unit

        override fun onActivityStopped(activity: Activity) = Unit

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) = Unit

        override fun onActivityDestroyed(activity: Activity) = Unit
    }
    
    fun init(app: ConverterApp) {
        DaggerAppComponent
                .builder()
                .application(app)
                .build()
                .inject(app)

        app.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
            if (f is Injectable) {
                AndroidSupportInjection.inject(f)
            }
        }
    }
    
    private fun handleActivity(activity: Activity) {
        if (activity is HasSupportFragmentInjector) {
            AndroidInjection.inject(activity)
        }
        
        activity.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks)
    }
}