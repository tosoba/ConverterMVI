package com.example.there.convertermvi.data.cache.converter

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExchangeRatesTypeConverter {
    @TypeConverter
    fun toExchangeRates(str: String): Map<String, Double> = Gson().fromJson(str, object : TypeToken<Map<String, Double>>() {}.type)

    @TypeConverter
    fun fromExchangeRates(er: Map<String, Double>): String = Gson().toJson(er)
}