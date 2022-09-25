package com.example.there.convertermvi.util

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

fun Activity.registerFragmentLifecycleCallbacks(
    fragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks,
    recursive: Boolean = true
) = (this as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
    fragmentLifecycleCallbacks,
    recursive
)
