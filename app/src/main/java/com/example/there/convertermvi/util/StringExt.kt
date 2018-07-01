package com.example.there.convertermvi.util

val String.toDoubleZeroIfEmpty: Double
    get() = if (isBlank()) 0.0 else toDouble()