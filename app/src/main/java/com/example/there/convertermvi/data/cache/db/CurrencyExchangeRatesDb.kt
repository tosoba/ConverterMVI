package com.example.there.convertermvi.data.cache.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.there.convertermvi.data.cache.converter.DateTypeConverters
import com.example.there.convertermvi.data.cache.converter.ExchangeRatesTypeConverter
import com.example.there.convertermvi.data.cache.dao.CurrencyExchangeRatesDao
import com.example.there.convertermvi.data.model.CurrencyExchangeRates

@Database(
        entities = [CurrencyExchangeRates::class],
        version = 1
)
@TypeConverters(value = [DateTypeConverters::class, ExchangeRatesTypeConverter::class])
abstract class CurrencyExchangeRatesDb : RoomDatabase() {
    abstract fun currencyExchangeRatesDao(): CurrencyExchangeRatesDao
}