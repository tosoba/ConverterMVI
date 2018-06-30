package com.example.there.convertermvi.data.cache.converter

import android.arch.persistence.room.TypeConverter
import com.example.there.convertermvi.data.model.ExchangeRates
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExchangeRatesTypeConverter {
    @TypeConverter
    fun toExchangeRates(str: String): ExchangeRates = Gson().fromJson(str, object : TypeToken<ExchangeRates>() {}.type)

    @TypeConverter
    fun fromExchangeRates(er: ExchangeRates): String = Gson().toJson(er)
}