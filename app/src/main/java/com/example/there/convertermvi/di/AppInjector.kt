package com.example.there.convertermvi.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.there.convertermvi.ConverterApp
import com.example.there.convertermvi.util.registerFragmentLifecycleCallbacks
import dagger.android.support.AndroidSupportInjection

object AppInjector {
    private val activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) =
            handleActivityCreated(activity)

        override fun onActivityStarted(activity: Activity) = Unit

        override fun onActivityResumed(activity: Activity) = Unit

        override fun onActivityPaused(activity: Activity) = Unit

        override fun onActivityStopped(activity: Activity) = Unit

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

        override fun onActivityDestroyed(activity: Activity) = Unit
    }

    fun init(app: ConverterApp) {
        app.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    private fun handleActivityCreated(activity: Activity) {
        activity.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                if (f is Injectable) AndroidSupportInjection.inject(f)
            }
        }, true)
    }
}