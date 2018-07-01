package com.example.there.convertermvi.data.model

import android.annotation.SuppressLint
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.example.there.convertermvi.data.cache.converter.DateTypeConverters
import com.example.there.convertermvi.data.cache.converter.ExchangeRatesTypeConverter
import com.example.there.convertermvi.data.cache.db.Tables
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = Tables.CURRENCY_EXCHANGE_RATES_TABLE)
data class CurrencyExchangeRates(
    @PrimaryKey
    val base: String,

    @TypeConverters(DateTypeConverters::class)
    val date: Date,

    @TypeConverters(ExchangeRatesTypeConverter::class)
    val rates: Map<String, Double>
) {
    val isValid: Boolean
        @SuppressLint("SimpleDateFormat")
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(date) == sdf.format(Calendar.getInstance().time)
        }
}

