package com.example.there.convertermvi.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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