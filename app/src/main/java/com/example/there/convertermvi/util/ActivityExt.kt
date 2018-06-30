package com.example.there.convertermvi.util

import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

fun Activity.registerFragmentLifecycleCallbacks(
        fragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks,
        recursive: Boolean = true
) = (this as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, recursive)
